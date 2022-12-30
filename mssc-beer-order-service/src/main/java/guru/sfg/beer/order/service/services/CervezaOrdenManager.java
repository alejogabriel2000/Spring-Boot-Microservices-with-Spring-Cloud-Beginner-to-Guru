package guru.sfg.beer.order.service.services;

import java.util.UUID;

import guru.sfg.beer.order.service.domain.BeerOrder;

public interface CervezaOrdenManager {

   BeerOrder nuevaOrdenCerveza(BeerOrder beerOrder);

   void procesarValidacionResponse(UUID cervezaOrdenId, Boolean esValido);
}
