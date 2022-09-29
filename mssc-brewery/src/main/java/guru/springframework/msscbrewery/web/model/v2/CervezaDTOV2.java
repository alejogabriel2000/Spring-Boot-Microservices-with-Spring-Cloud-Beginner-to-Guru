package guru.springframework.msscbrewery.web.model.v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CervezaDTOV2 {

   //@Null
   private UUID id;

   //@NotBlank
   private String nombreCerveza;

   //@NotBlank
   private CervezaEstiloEnum estiloCerveza;

   //@Positive
   private Long upc;

   private OffsetDateTime fechaCreacion;

   private OffsetDateTime fechaUltModificacion;
}
