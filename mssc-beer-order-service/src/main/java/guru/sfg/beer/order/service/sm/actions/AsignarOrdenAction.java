package guru.sfg.beer.order.service.sm.actions;

import java.util.UUID;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.OrdenEstadoCervezaEnum;
import guru.sfg.beer.order.service.domain.OrdenEventoCervezaEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.services.CervezaOrdenManagerImpl;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import guru.sfg.brewery.model.events.ValidarOrdenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsignarOrdenAction implements Action<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> {

   private final BeerOrderRepository beerOrderRepository;
   private final BeerOrderMapper beerOrderMapper;
   private final JmsTemplate jmsTemplate;

   @Override
   public void execute(StateContext<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> stateContext) {

      String cervezaOrdenId = (String) stateContext.getMessage().getHeaders().get(CervezaOrdenManagerImpl.ORDEN_ID_HEADER);
      BeerOrder cervezaOrden = beerOrderRepository.findOneById(UUID.fromString(cervezaOrdenId));

      jmsTemplate.convertAndSend(JmsConfig.ASIGNAR_ORDEN_QUEUE, beerOrderMapper.beerOrderToDto(cervezaOrden));

      log.debug("Envio request de asignacion a la queue para la orden Id: " + cervezaOrdenId);
   }
}
