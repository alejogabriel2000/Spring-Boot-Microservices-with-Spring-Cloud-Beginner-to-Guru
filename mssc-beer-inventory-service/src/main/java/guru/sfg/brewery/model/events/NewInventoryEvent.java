package guru.sfg.brewery.model.events;

import guru.sfg.common.events.BeerDto;
import guru.sfg.common.events.BeerEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NewInventoryEvent extends BeerEvent {
   public NewInventoryEvent(BeerDto beerDto) {
      super(beerDto);
   }
}