package guru.sfg.beer.order.service.services;

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
import lombok.RequiredArgsConstructor;

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

   private void enviarCervezaOrdenEvento(BeerOrder beerOrder, OrdenEventoCervezaEnum ordenEventoCervezaEnum ) {
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

      return sm;

   }
}
