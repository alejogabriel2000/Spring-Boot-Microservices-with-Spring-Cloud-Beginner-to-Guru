package guru.sfg.beer.order.service.web.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CervezaDTO {

   private UUID id;

   private String beerName;

   private String beerStyle;

   private BigDecimal price;


}
