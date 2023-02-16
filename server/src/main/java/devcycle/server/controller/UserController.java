package devcycle.server.controller;

import devcycle.server.domain.user.User;
import devcycle.server.dto.user.*;
import devcycle.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequestDto dto) throws Exception {
        User user = userService.signup(dto);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/login")
    public TokenInfo login(@RequestBody LoginDto dto) {
        return userService.login(dto);
    }

    @GetMapping("/test")
    public String test() {
        return "test success";
    }

    @PostMapping("/signout")
    public String signout() {
        // 로그아웃 실행
        userService.signout();
        return "success";
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenInfo> reissue(@RequestBody TokenRequestDto dto) {
        TokenInfo tokenInfo = userService.reissue(dto);
        return ResponseEntity.ok().body(tokenInfo);
    }

    @PostMapping("/find/id")
    public ResponseEntity<List<String>> findEmail(@RequestBody FindEmailRequestDto dto) {
        List<String> emailList = userService.findEmailByNameAndBirth(dto);
        return ResponseEntity.ok().body(emailList);
    }

    @PostMapping("/find/pw")
    public ResponseEntity<Boolean> findPassword(@RequestBody FindPasswordRequestDto dto) {
        userService.findPasswordByEmailAndName(dto);
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserInfo> getUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok().body(userService.getUserInfo(request));
    }

    @PostMapping("/change/pw")
    public ResponseEntity<Boolean> changePassword(@RequestHeader Map<String, String> requestHeader, @RequestBody ChangePasswordDto dto) {
        userService.changePassword(requestHeader, dto);
        return ResponseEntity.ok().body(true);
    }
}
