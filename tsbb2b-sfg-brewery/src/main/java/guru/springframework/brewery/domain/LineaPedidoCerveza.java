package guru.springframework.brewery.domain;

import java.sql.Timestamp;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class LineaPedidoCerveza extends BaseEntity {

   @Builder
   public LineaPedidoCerveza(UUID id, Long version, Timestamp creacionFecha, Timestamp ultimaModificacionFecha,
                             CervezaOrden cervezaOrden, Cerveza cerveza, Integer ordenCantidad,
                             Integer cantidadAsignada) {
      super(id, version, creacionFecha, ultimaModificacionFecha);
      this.cervezaOrden = cervezaOrden;
      this.cerveza = cerveza;
      this.ordenCantidad = ordenCantidad;
      this.cantidadAsignada = cantidadAsignada;
   }

   @ManyToOne
   private CervezaOrden cervezaOrden;

   @ManyToOne
   private Cerveza cerveza;

   private Integer ordenCantidad = 0;
   private Integer cantidadAsignada = 0;
}