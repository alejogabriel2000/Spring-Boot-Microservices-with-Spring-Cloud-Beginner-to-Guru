package guru.sfg.brewery.model;

import guru.sfg.brewery.model.events.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidarOrdenRequest {

   private BeerOrderDto cervezaOrden;
}
