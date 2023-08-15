package guru.springframework.brewery.web.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrdenEstadoActualizacion extends BaseItem {

   private UUID ordenId;
   private String clienteRef;
   private String ordenEstado;

   @Builder
   public OrdenEstadoActualizacion(UUID id, Integer version, OffsetDateTime creacionFecha, OffsetDateTime ultimaModificacionFecha,
                                   UUID ordenId, String ordenEstado, String clienteRef) {
      super(id, version, creacionFecha, ultimaModificacionFecha);
      this.ordenId = ordenId;
      this.ordenEstado = ordenEstado;
      this.clienteRef = clienteRef;
   }




}
