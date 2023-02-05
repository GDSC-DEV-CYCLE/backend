package devcycle.server.service;

import devcycle.server.config.JwtTokenProvider;
import devcycle.server.domain.user.User;
import devcycle.server.domain.user.UserRepository;
import devcycle.server.dto.user.LoginDto;
import devcycle.server.dto.user.SignupRequestDto;
import devcycle.server.dto.user.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public User signup(SignupRequestDto dto) throws Exception {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }
        if (!dto.getPassword().equals(dto.getCheckPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
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

        return tokenInfo;
    }

}
