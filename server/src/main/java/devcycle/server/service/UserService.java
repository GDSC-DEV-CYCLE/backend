package devcycle.server.service;

import devcycle.server.config.JwtTokenProvider;
import devcycle.server.domain.user.User;
import devcycle.server.domain.user.UserRepository;
import devcycle.server.dto.user.LoginDto;
import devcycle.server.dto.user.SignupRequestDto;
import devcycle.server.dto.user.TokenInfo;
import devcycle.server.dto.user.TokenRequestDto;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    public User signup(SignupRequestDto dto) {
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
        return user;
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

    public void signout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (Objects.equals(email, "anonymousUser")) {
            throw new RuntimeException("로그인하지 않았습니다.");
        }
        String accessToken = (String) authentication.getCredentials();
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new JwtException("유효한 토큰이 아닙니다.");
        }
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
        redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpiration(),TimeUnit.MILLISECONDS);

        return tokenInfo;
    }

}
