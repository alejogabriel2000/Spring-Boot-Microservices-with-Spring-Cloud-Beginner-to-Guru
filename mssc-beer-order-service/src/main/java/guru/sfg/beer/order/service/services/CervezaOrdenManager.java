package guru.sfg.beer.order.service.services;

import java.util.UUID;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.brewery.model.BeerOrderDto;

public interface CervezaOrdenManager {

   BeerOrder nuevaOrdenCerveza(BeerOrder beerOrder);

   void procesarValidacionResponse(UUID cervezaOrdenId, Boolean esValido);

   void cervezaOrdenUbicacionAprobado(BeerOrderDto cervezaOrden);

   void cervezaOrdenUbicacionPendienteInventario(BeerOrderDto cervezaOrden);

   void cervezaOrdenUbicacionError(BeerOrderDto cervezaOrden);

   void cervezaOrdenTomar(UUID id);

   void cancelarOrden(UUID id);
}