package devcycle.server.dto.user;

import devcycle.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
    private String email;
    private String password;
    private String checkPassword;
    private String name;
    private String birth;

    @Builder
    public SignupRequestDto(String email, String password, String checkPassword, String name, String birth) {
        this.email = email;
        this.password = password;
        this.checkPassword = checkPassword;
        this.name = name;
        this.birth = birth;
    }

    public User toEntity() {
        return User.builder().email(email).password(password).name(name).birth(birth).build();
    }
}
