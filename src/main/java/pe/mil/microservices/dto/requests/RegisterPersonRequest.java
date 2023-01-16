package pe.mil.microservices.dto.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterPersonRequest implements Serializable {

    private static final long serialVersionUID = 5230901019196076399L;

    @NotNull
    private Long personId;

    @NotBlank
    @Size(min = 1)
    private String name;

    @NotBlank
    @Size(min = 1)
    private String lastName;

    @NotBlank
    @Size(max = 8, min = 8)
    private String dni;
}
