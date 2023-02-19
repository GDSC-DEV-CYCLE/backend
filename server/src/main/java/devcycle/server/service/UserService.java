package devcycle.server.service;

import devcycle.server.config.JwtTokenProvider;
import devcycle.server.domain.user.User;
import devcycle.server.domain.user.UserRepository;
import devcycle.server.dto.user.*;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final JavaMailSender javaMailSender;

    public void signup(SignupRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        if (!dto.getPassword().equals(dto.getCheckPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        User user = dto.toEntity();
        user.encodePassword(passwordEncoder);
        user.updateRole();
        userRepository.save(user);
    }

    public TokenInfo login(LoginDto dto) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 4. RefreshToken Redis 저장
        redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpiration(), TimeUnit.MILLISECONDS);
        return tokenInfo;
    }

    public void signout(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").substring(7);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new JwtException("유효한 토큰이 아닙니다.");
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        String email = authentication.getName();
        userRepository.deleteByEmail(email);
    }

    public void logout(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").substring(7);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new JwtException("유효한 토큰이 아닙니다.");
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            redisTemplate.delete("RT:" + authentication.getName());
        }
        Long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    public TokenInfo reissue(TokenRequestDto dto) {
        if (!jwtTokenProvider.validateToken(dto.getRefreshToken())) {
            throw new JwtException("유효한 토큰이 아닙니다.");
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(dto.getAccessToken());
        String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + authentication.getName());
        if (ObjectUtils.isEmpty(refreshToken)) {
            throw new RuntimeException("로그아웃된 유저입니다.");
        }
        if (!refreshToken.equals(dto.getRefreshToken())) {
            throw new RuntimeException("토큰 정보가 일치하지 않습니다.");
        }
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpiration(), TimeUnit.MILLISECONDS);

        return tokenInfo;
    }

    public List<String> findEmailByNameAndBirth(FindEmailRequestDto dto) {
        List<User> userList = userRepository.findByNameAndBirth(dto.getName(), dto.getBirth());
        if (userList.isEmpty()) {
            throw new RuntimeException("회원정보가 존재하지 않습니다.");
        }
        List<String> emailList = userList.stream().map(User::getEmail).collect(Collectors.toList());
        return emailList;
    }

    public void findPasswordByEmailAndName(FindPasswordRequestDto dto) {
        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if (user.isEmpty() || !user.get().getName().equals(dto.getName())) {
            throw new RuntimeException("회원정보가 존재하지 않습니다.");
        }
        String tempPassword = getTempPassword(10);
        user.get().setTempPassword(passwordEncoder, tempPassword);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.getEmail());
        message.setSubject("[dev.cycle] 임시 비밀번호 안내");
        message.setText("안녕하세요, " + dto.getName() + "님\n" + "임시 비밀번호는 " + tempPassword + " 입니다.");
        javaMailSender.send(message);
    }

    public String getTempPassword(int size) {
        char[] charSet = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&'};

        StringBuilder stringBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(new Date().getTime());

        int idx = 0;
        int len = charSet.length;
        for (int i = 0; i < size; i++) {
            idx = secureRandom.nextInt(len);
            stringBuilder.append(charSet[idx]);
        }

        return stringBuilder.toString();
    }

    public UserInfo getUserInfo(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").substring(7);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new JwtException("유효한 토큰이 아닙니다.");
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        User user;
        try {
            user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }
        return UserInfo.builder().name(user.getName()).email(user.getEmail()).birth(user.getBirth()).job(user.getJob()).build();
    }
}
