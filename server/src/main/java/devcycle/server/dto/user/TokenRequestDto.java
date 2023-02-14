package devcycle.server.dto.user;

import lombok.Getter;

@Getter
public class TokenRequestDto {
    String accessToken;
    String refreshToken;
}
