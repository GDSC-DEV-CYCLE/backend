package devcycle.server.dto.user;

import devcycle.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
    @NotBlank(message = "EMAIL_IS_MANDATORY")
    private String email;

    @NotBlank(message = "PASSWORD_IS_MANDATORY")
    private String password;

    @NotBlank(message = "PASSWORD_IS_MANDATORY")
    private String checkPassword;

    @NotBlank(message = "NAME_IS_MANDATORY")
    private String name;

    @NotBlank(message = "BIRTH_IS_MANDATORY")
    private String birth;

    @NotBlank(message = "JOB_IS_MANDATORY")
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
