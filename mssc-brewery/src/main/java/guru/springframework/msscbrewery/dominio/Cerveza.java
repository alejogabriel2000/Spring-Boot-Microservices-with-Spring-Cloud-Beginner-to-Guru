package guru.springframework.msscbrewery.dominio;

import guru.springframework.msscbrewery.web.model.v2.CervezaEstiloEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cerveza {

   private UUID id;
   private String nombreCerveza;
   private CervezaEstiloEnum estiloCerveza;
   private Long upc;

   private Timestamp fechaCreacion;
   private Timestamp fechaUltModificacion;
}
