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
public class CervezaInventario extends BaseEntity {

   @Builder
   public CervezaInventario(UUID id, Long version, Timestamp creacionFecha, Timestamp ultimaModificacionFecha, Cerveza cerveza,
                            Integer quantityOnHand) {
      super(id, version, creacionFecha, ultimaModificacionFecha);
      this.cerveza = cerveza;
      this.quantityOnHand = quantityOnHand;
   }

   @ManyToOne
   private Cerveza cerveza;

   private Integer quantityOnHand = 0;
}
