package guru.sfg.beer.order.service.sm;

import java.util.Optional;
import java.util.UUID;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.OrdenEstadoCervezaEnum;
import guru.sfg.beer.order.service.domain.OrdenEventoCervezaEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.services.CervezaOrdenManagerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CervezaOrdenEstadoCambioInterceptor extends StateMachineInterceptorAdapter<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> {

   private final BeerOrderRepository beerOrderRepository;

   @Override
   public void preStateChange(State<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> state, Message<OrdenEventoCervezaEnum> message,
                              Transition<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> transition,
                              StateMachine<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> stateMachine) {

      //CervezaOrdenManagerImpl.ORDEN_ID_HEADER

      Optional.ofNullable(message)
         .flatMap(msg -> Optional.ofNullable((String) msg.getHeaders().getOrDefault(CervezaOrdenManagerImpl.ORDEN_ID_HEADER, " ")))
         .ifPresent(ordenId -> {
            log.debug("Guardando estado para lo orden con Id: " + ordenId + " Estado: " + state.getId());

            BeerOrder cervezaOrden = beerOrderRepository.getOne(UUID.fromString(ordenId));
            cervezaOrden.setOrderStatus(state.getId());
            beerOrderRepository.saveAndFlush(cervezaOrden);
         });


   }
}
