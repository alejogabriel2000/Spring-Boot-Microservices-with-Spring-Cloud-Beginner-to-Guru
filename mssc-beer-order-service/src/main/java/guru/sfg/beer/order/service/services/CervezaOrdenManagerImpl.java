package guru.sfg.beer.order.service.services;

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
      BeerOrder ordenCervezaGuardada = beerOrderRepository.save(beerOrder);
      enviarCervezaOrdenEvento(ordenCervezaGuardada, OrdenEventoCervezaEnum.VALIDAR_PEDIDO);
      return ordenCervezaGuardada;
   }

   @Override
   public void procesarValidacionResponse(UUID cervezaOrdenId, Boolean esValido) {
      BeerOrder beerOrder = beerOrderRepository.getOne(cervezaOrdenId);
      if (esValido) {
         enviarCervezaOrdenEvento(beerOrder, OrdenEventoCervezaEnum.VALIDACION_APROBADA);
         BeerOrder ordenValidada = beerOrderRepository.findOneById(cervezaOrdenId);
         enviarCervezaOrdenEvento(beerOrder, OrdenEventoCervezaEnum.ASIGNAR_PEDIDO);
      } else {
         enviarCervezaOrdenEvento(beerOrder, OrdenEventoCervezaEnum.VALIDACION_FALLIDA);
      }
   }

   @Override
   public void cervezaOrdenUbicacionAprobado(BeerOrderDto cervezaOrdenDto) {
      BeerOrder cervezaOrden = beerOrderRepository.getOne(cervezaOrdenDto.getId());
      enviarCervezaOrdenEvento(cervezaOrden, OrdenEventoCervezaEnum.ASIGNACION_EXITOSA);
      actualizarUbicacionCantidad(cervezaOrdenDto, cervezaOrden);
   }

   private void actualizarUbicacionCantidad(BeerOrderDto cervezaOrdenDto, BeerOrder cervezaOrden) {
      BeerOrder ubicacionOrden = beerOrderRepository.getOne(cervezaOrdenDto.getId());

      ubicacionOrden.getBeerOrderLines().forEach(cervezaOrdenLine -> {
         cervezaOrdenDto.getBeerOrderLines().forEach(cervezaOrdenLineDto -> {
            if (cervezaOrdenLine.getId().equals(cervezaOrdenLineDto.getId())) {
               cervezaOrdenLine.setQuantityAllocated(cervezaOrdenLineDto.getQuantityAllocated());
            }
         });
      });
      beerOrderRepository.saveAndFlush(cervezaOrden);
   }

   @Override
   public void cervezaOrdenUbicaionPendienteInventario(BeerOrderDto cervezaOrdenDto) {
      BeerOrder cervezaOrden = beerOrderRepository.getOne(cervezaOrdenDto.getId());
      enviarCervezaOrdenEvento(cervezaOrden, OrdenEventoCervezaEnum.ASIGNACION_SIN_INVENTARIO);
      actualizarUbicacionCantidad(cervezaOrdenDto, cervezaOrden);
   }

   @Override
   public void cervezaOrdenUbicacionError(BeerOrderDto cervezaOrdenDto) {
      BeerOrder cervezaOrden = beerOrderRepository.getOne(cervezaOrdenDto.getId());
      enviarCervezaOrdenEvento(cervezaOrden, OrdenEventoCervezaEnum.ASIGNACION_FALLIDA);
   }

   private void enviarCervezaOrdenEvento(BeerOrder beerOrder, OrdenEventoCervezaEnum ordenEventoCervezaEnum) {
      StateMachine<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> sm = build(beerOrder);
      Message msg = MessageBuilder.withPayload(ordenEventoCervezaEnum).setHeader(ORDEN_ID_HEADER, beerOrder.getId().toString()).build();
      sm.sendEvent(msg);
   }

   private StateMachine<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> build(BeerOrder beerOrder) {
      StateMachine<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> sm = stateMachineFactory.getStateMachine(beerOrder.getId());
      sm.stop();
      sm.getStateMachineAccessor().doWithAllRegions(sma -> {
         sma.addStateMachineInterceptor(cervezaOrdenEstadoCambioInterceptor);
         sma.resetStateMachine(new DefaultStateMachineContext<>(beerOrder.getOrderStatus(), null, null, null));
      });
      return sm;
   }
}
