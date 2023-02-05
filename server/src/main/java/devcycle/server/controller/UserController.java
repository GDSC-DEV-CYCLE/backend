package devcycle.server.controller;

import devcycle.server.config.JwtTokenProvider;
import devcycle.server.domain.user.User;
import devcycle.server.domain.user.UserRepository;
import devcycle.server.domain.user.UserRole;
import devcycle.server.dto.user.LoginDto;
import devcycle.server.dto.user.SignupRequestDto;
import devcycle.server.dto.user.TokenInfo;
import devcycle.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequestDto dto) throws Exception {
        User user = userService.signup(dto);
        return ResponseEntity.ok().body(user);
    }

    // 로그인
    @PostMapping("/login")
    public TokenInfo login(@RequestBody LoginDto dto) {
        return userService.login(dto);
    }

    @GetMapping("/test")
    public String test() {
        return "test success";
    }
}
