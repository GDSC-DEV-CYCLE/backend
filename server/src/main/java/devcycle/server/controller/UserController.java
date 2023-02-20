package devcycle.server.controller;

import devcycle.server.dto.user.*;
import devcycle.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<Boolean> signup(@Validated @RequestBody SignupRequestDto dto) throws Exception {
        userService.signup(dto);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/login")
    public TokenInfo login(@Validated @RequestBody LoginDto dto) {
        return userService.login(dto);
    }

    @PostMapping("/signout")
    public ResponseEntity<Boolean> signout(HttpServletRequest request) {
        userService.logout(request);
        userService.signout(request);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenInfo> reissue(@Validated @RequestBody TokenRequestDto dto) {
        TokenInfo tokenInfo = userService.reissue(dto);
        return ResponseEntity.ok().body(tokenInfo);
    }

    @PostMapping("/find/id")
    public ResponseEntity<List<String>> findEmail(@Validated @RequestBody FindEmailRequestDto dto) {
        List<String> emailList = userService.findEmailByNameAndBirth(dto);
        return ResponseEntity.ok().body(emailList);
    }

    @PostMapping("/find/pw")
    public ResponseEntity<Boolean> findPassword(@Validated @RequestBody FindPasswordRequestDto dto) {
        userService.findPasswordByEmailAndName(dto);
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserInfo> getUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok().body(userService.getUserInfo(request));
    }

    @PostMapping("/change/pw")
    public ResponseEntity<Boolean> changePassword(@RequestHeader Map<String, String> requestHeader, @Validated @RequestBody ChangePasswordDto dto) {
        userService.changePassword(requestHeader, dto);
        return ResponseEntity.ok().body(true);
    }
}
