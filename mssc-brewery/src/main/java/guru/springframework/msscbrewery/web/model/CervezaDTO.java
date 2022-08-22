package guru.springframework.msscbrewery.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CervezaDTO {

    @Null
    private UUID id;

    @NotBlank
    private String nombreCerveza;

    @NotBlank
    private String estiloCerveza;

    @Positive
    private Long upc;
}
