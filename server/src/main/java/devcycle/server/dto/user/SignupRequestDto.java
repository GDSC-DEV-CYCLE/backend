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
    private String job;

    @Builder
    public SignupRequestDto(String email, String password, String checkPassword, String name, String birth, String job) {
        this.email = email;
        this.password = password;
        this.checkPassword = checkPassword;
        this.name = name;
        this.birth = birth;
        this.job = job;
    }

    public User toEntity() {
        return User.builder().email(email).password(password).name(name).birth(birth).job(job).build();
    }
}
