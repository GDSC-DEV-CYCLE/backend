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
public class FindPasswordRequestDto {
    @NotBlank(message = "EMAIL_IS_MANDATORY")
    String email;

    @NotBlank(message = "NAME_IS_MANDATORY")
    String name;
}
