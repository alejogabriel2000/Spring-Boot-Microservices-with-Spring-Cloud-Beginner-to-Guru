package guru.sfg.beer.inventory.service.services;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AsignacionServiceImpl implements AsignacionService {

   private final BeerInventoryRepository beerInventoryRepository;

   @Override
   public Boolean asignarOrden(BeerOrderDto cervezaOrdenDto) {
      log.debug("Asignacion de ordenId: " + cervezaOrdenDto.getId());

      AtomicInteger totalOrdenada = new AtomicInteger();
      AtomicInteger totalAsignado = new AtomicInteger();

      cervezaOrdenDto.getBeerOrderLines().forEach(beerOrdenLine -> {
         if ((((beerOrdenLine.getOrderQuantity() != null ? beerOrdenLine.getOrderQuantity() : 0)
            - (beerOrdenLine.getQuantityAllocated() != null ? beerOrdenLine.getQuantityAllocated() : 0 )) > 0 )) {
            asignarLineaPedidoCerveza(beerOrdenLine);
         }
         totalOrdenada.set(totalOrdenada.get() + beerOrdenLine.getOrderQuantity());
         totalAsignado.set(totalAsignado.get() + (beerOrdenLine.getQuantityAllocated() != null ? beerOrdenLine.getQuantityAllocated() : 0));

      });

      log.debug("Total ordenado: " + totalOrdenada.get() + " Total asignado: " + totalAsignado.get());
      return totalOrdenada.get() == totalAsignado.get();
   }

   @Override
   public void desasignarOrden(BeerOrderDto cervezaOrdenDto) {
      cervezaOrdenDto.getBeerOrderLines().forEach(cervezaOrdenLineDto -> {
         BeerInventory cervezaInventario = BeerInventory.builder()
            .beerId(cervezaOrdenLineDto.getBeerId())
            .upc(cervezaOrdenLineDto.getUpc())
            .quantityOnHand(cervezaOrdenLineDto.getQuantityAllocated())
            .build();
         BeerInventory inventarioGuardado = beerInventoryRepository.save(cervezaInventario);
         log.debug("inventario Guardado para la cerveza upc: " + inventarioGuardado.getUpc() + " inventario id: " + inventarioGuardado.getId());
      });
   }

   private void asignarLineaPedidoCerveza(BeerOrderLineDto beerOrdenLine) {
      List<BeerInventory> cervezaInventariosList = beerInventoryRepository.findAllByUpc(beerOrdenLine.getUpc());

      cervezaInventariosList.forEach(cervezaInventario -> {
         int inventario = (cervezaInventario.getQuantityOnHand() == null) ? 0 : cervezaInventario.getQuantityOnHand();
         int ordenCantidad = (beerOrdenLine.getOrderQuantity() == null) ? 0 : beerOrdenLine.getOrderQuantity();
         int asignadoCantidad = (beerOrdenLine.getQuantityAllocated() == null) ? 0 : beerOrdenLine.getQuantityAllocated();
         int cantidadAAsignar = ordenCantidad - asignadoCantidad;

         if (inventario >= cantidadAAsignar) { // asignacion total
            inventario = inventario - cantidadAAsignar;
            beerOrdenLine.setQuantityAllocated(ordenCantidad);
            cervezaInventario.setQuantityOnHand(inventario);

            beerInventoryRepository.save(cervezaInventario);
         } else if (inventario > 0 ) {    //asignacion parcial
            beerOrdenLine.setQuantityAllocated(asignadoCantidad + inventario);
            cervezaInventario.setQuantityOnHand(0);

            beerInventoryRepository.delete(cervezaInventario);
         }
      });
   }
}