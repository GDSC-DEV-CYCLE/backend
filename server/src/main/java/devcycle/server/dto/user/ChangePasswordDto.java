package devcycle.server.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDto {
    @NotBlank(message = "PASSWORD_IS_MANDATORY")
    String password;

    @NotBlank(message = "PASSWORD_IS_MANDATORY")
    String checkPassword;
}
