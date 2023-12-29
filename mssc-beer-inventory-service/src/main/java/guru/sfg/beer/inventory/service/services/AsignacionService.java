package guru.sfg.beer.inventory.service.services;

import guru.sfg.brewery.model.BeerOrderDto;

public interface AsignacionService {

   Boolean asignarOrden(BeerOrderDto cervezaOrdenDto);

   void desasignarOrden(BeerOrderDto cervezaOrdenDto);
}