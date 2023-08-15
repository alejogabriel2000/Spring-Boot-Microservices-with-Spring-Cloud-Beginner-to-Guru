package guru.springframework.brewery.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import guru.springframework.brewery.web.model.CervezaEstiloEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Cerveza extends BaseEntity {

   @Builder
   public Cerveza(UUID id, Long version, Timestamp creacionFecha, Timestamp ultimaModificacionFecha, String beerName, CervezaEstiloEnum cervezaEstilo,
                  Long upc, Integer minOnHand, Integer quantityToBrew, BigDecimal price, Set<CervezaInventario> cervezaInventario) {
      super(id, version, creacionFecha, ultimaModificacionFecha);
      this.beerName = beerName;
      this.cervezaEstilo = cervezaEstilo;
      this.upc = upc;
      this.minOnHand = minOnHand;
      this.quantityToBrew = quantityToBrew;
      this.price = price;
      this.cervezaInventario = cervezaInventario;
   }

   private String beerName;
   private CervezaEstiloEnum cervezaEstilo;

   @Column(unique = true)
   private Long upc;

   /**
    * Min on hand qty - used to trigger brew
    */
   private Integer minOnHand;
   private Integer quantityToBrew;
   private BigDecimal price;

   @OneToMany(mappedBy = "beer")
   private Set<CervezaInventario> cervezaInventario;
}