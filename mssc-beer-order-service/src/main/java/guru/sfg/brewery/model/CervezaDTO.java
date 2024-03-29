package guru.sfg.brewery.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CervezaDTO {

   private UUID id;

   private String beerName;

   private String beerStyle;

   private String upc;

   private BigDecimal price;

}
