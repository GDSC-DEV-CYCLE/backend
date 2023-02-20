package devcycle.server.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LoginDto {
    @NotBlank(message = "EMAIL_IS_MANDATORY")
    private String email;

    @NotBlank(message = "PASSWORD_IS_MANDATORY")
    private String password;

    @Builder
    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
