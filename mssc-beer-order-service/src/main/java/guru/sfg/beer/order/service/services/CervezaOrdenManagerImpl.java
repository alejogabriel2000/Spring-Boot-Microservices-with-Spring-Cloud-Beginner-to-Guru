package guru.sfg.beer.order.service.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.OrdenEstadoCervezaEnum;
import guru.sfg.beer.order.service.domain.OrdenEventoCervezaEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.sm.CervezaOrdenEstadoCambioInterceptor;
import guru.sfg.brewery.model.BeerOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CervezaOrdenManagerImpl implements CervezaOrdenManager {

   public static final String ORDEN_ID_HEADER = "ORDEN_ID_HEADER";
   private final StateMachineFactory<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> stateMachineFactory;
   private final BeerOrderRepository beerOrderRepository;
   private final CervezaOrdenEstadoCambioInterceptor cervezaOrdenEstadoCambioInterceptor;

   @Transactional
   @Override
   public BeerOrder nuevaOrdenCerveza(BeerOrder beerOrder) {
      beerOrder.setId(null);
      beerOrder.setOrderStatus(OrdenEstadoCervezaEnum.NUEVO);
      BeerOrder ordenCervezaGuardada = beerOrderRepository.saveAndFlush(beerOrder);
      enviarCervezaOrdenEvento(ordenCervezaGuardada, OrdenEventoCervezaEnum.VALIDAR_PEDIDO);
      return ordenCervezaGuardada;
   }

   @Transactional
   @Override
   public void procesarValidacionResponse(UUID cervezaOrdenId, Boolean esValido) {

      Optional<BeerOrder> cervezaOrdenOptional = beerOrderRepository.findById(cervezaOrdenId);

      cervezaOrdenOptional.ifPresentOrElse(cervezaOrden -> {
         if (esValido) {
            enviarCervezaOrdenEvento(cervezaOrden, OrdenEventoCervezaEnum.VALIDACION_APROBADA);
            BeerOrder ordenValidada = beerOrderRepository.findById(cervezaOrdenId).get();
            enviarCervezaOrdenEvento(ordenValidada, OrdenEventoCervezaEnum.ASIGNAR_PEDIDO);
         } else {
            enviarCervezaOrdenEvento(cervezaOrden, OrdenEventoCervezaEnum.VALIDACION_FALLIDA);
         }
      }, () -> log.error("Orden no encontrada. Id: " + cervezaOrdenId));
   }

   @Override
   public void cervezaOrdenUbicacionAprobado(BeerOrderDto cervezaOrdenDto) {
      Optional<BeerOrder> cervezaOrdenOptional = beerOrderRepository.findById(cervezaOrdenDto.getId());
      cervezaOrdenOptional.ifPresentOrElse(cervezaOrden -> {
         enviarCervezaOrdenEvento(cervezaOrden, OrdenEventoCervezaEnum.ASIGNACION_EXITOSA);
         actualizarUbicacionCantidad(cervezaOrdenDto);
      }, () -> log.error("Orden no encontrada. Id: " + cervezaOrdenDto.getId()));
   }

   private void actualizarUbicacionCantidad(BeerOrderDto cervezaOrdenDto) {
      Optional<BeerOrder> ubicacionOrdenOptional = beerOrderRepository.findById(cervezaOrdenDto.getId());

      ubicacionOrdenOptional.ifPresentOrElse(ubicacionOrden -> {
         ubicacionOrden.getBeerOrderLines().forEach(cervezaOrdenLine -> {
            cervezaOrdenDto.getBeerOrderLines().forEach(cervezaOrdenLineDto -> {
               if (cervezaOrdenLine.getId().equals(cervezaOrdenLineDto.getId())) {
                  cervezaOrdenLine.setQuantityAllocated(cervezaOrdenLineDto.getQuantityAllocated());
               }
            });
         });
         beerOrderRepository.saveAndFlush(ubicacionOrden);

      }, () -> log.error("Orden no encontrada. Id: " + cervezaOrdenDto.getId()));
   }

   @Override
   public void cervezaOrdenUbicacionPendienteInventario(BeerOrderDto cervezaOrdenDto) {

      Optional<BeerOrder> cervezaOrdenOptional = beerOrderRepository.findById(cervezaOrdenDto.getId());

      cervezaOrdenOptional.ifPresentOrElse(cervezaOrden -> {
         enviarCervezaOrdenEvento(cervezaOrden, OrdenEventoCervezaEnum.ASIGNACION_SIN_INVENTARIO);
         actualizarUbicacionCantidad(cervezaOrdenDto);
      }, () -> log.error("Orden no encontrada. Id: " + cervezaOrdenDto.getId()));
   }

   @Override
   public void cervezaOrdenUbicacionError(BeerOrderDto cervezaOrdenDto) {
      Optional<BeerOrder> cervezaOrdenOptional = beerOrderRepository.findById(cervezaOrdenDto.getId());

      cervezaOrdenOptional.ifPresentOrElse(cervezaOrden -> {
         enviarCervezaOrdenEvento(cervezaOrden, OrdenEventoCervezaEnum.ASIGNACION_FALLIDA);
      }, () -> log.error("Orden no encontrada. Id: " + cervezaOrdenDto.getId()));
   }

   private void enviarCervezaOrdenEvento(BeerOrder beerOrder, OrdenEventoCervezaEnum ordenEventoCervezaEnum) {
      StateMachine<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> sm = build(beerOrder);
      Message msg = MessageBuilder.withPayload(ordenEventoCervezaEnum)
                                  .setHeader(ORDEN_ID_HEADER, beerOrder.getId().toString())
                                  .build();
      sm.sendEvent(msg);
   }

   private StateMachine<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> build(BeerOrder beerOrder) {
      StateMachine<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> sm = stateMachineFactory.getStateMachine(beerOrder.getId());
      sm.stop();
      sm.getStateMachineAccessor()
        .doWithAllRegions(sma -> {
         sma.addStateMachineInterceptor(cervezaOrdenEstadoCambioInterceptor);
         sma.resetStateMachine(new DefaultStateMachineContext<>(beerOrder.getOrderStatus(), null, null, null));
      });
      sm.start();
      return sm;
   }
}
