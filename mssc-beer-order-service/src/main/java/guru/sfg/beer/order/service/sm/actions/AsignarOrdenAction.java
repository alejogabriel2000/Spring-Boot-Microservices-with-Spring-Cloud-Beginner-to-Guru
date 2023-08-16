package guru.sfg.beer.order.service.sm.actions;

import java.util.Optional;
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
import guru.sfg.brewery.model.events.AsignarOrdenRequest;

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
      Optional<BeerOrder> cervezaOrdenOptional = beerOrderRepository.findById(UUID.fromString(cervezaOrdenId));
      cervezaOrdenOptional.ifPresentOrElse(cervezaOrden -> {
         jmsTemplate.convertAndSend(JmsConfig.ASIGNAR_ORDEN_QUEUE, AsignarOrdenRequest.builder()
                           .cervezaOrden(beerOrderMapper.beerOrderToDto(cervezaOrden))
                           .build());
         log.debug("Enviada ubicacion request para la orden con ID: " + cervezaOrdenId);
      }, () -> log.error("Orden de cerveza no encontrado"));
      log.debug("Envio request de asignacion a la queue para la orden Id: " + cervezaOrdenId);
   }
}