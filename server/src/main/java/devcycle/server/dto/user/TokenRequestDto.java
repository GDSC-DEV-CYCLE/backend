package devcycle.server.dto.user;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TokenRequestDto {
    @NotBlank(message = "ACCESS_TOKEN_IS_MANDATORY")
    String accessToken;

    @NotBlank(message = "REFRESH_TOKEN_IS_MANDATORY")
    String refreshToken;
}
