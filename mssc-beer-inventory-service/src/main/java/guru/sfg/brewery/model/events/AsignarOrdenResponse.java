package guru.sfg.brewery.model.events;

import guru.sfg.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignarOrdenResponse {
   private BeerOrderDto cervezaOrdenDto;
   private Boolean asignacionError = false;
   private Boolean pendienteInventario = false;
}
