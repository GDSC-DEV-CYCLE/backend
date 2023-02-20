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
public class FindEmailRequestDto {
    @NotBlank(message = "NAME_IS_MANDATORY")
    private String name;

    @NotBlank(message = "BIRTH_IS_MANDATORY")
    private String birth;
}
