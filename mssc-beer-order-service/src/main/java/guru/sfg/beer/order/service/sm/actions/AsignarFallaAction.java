package guru.sfg.beer.order.service.sm.actions;

import java.util.UUID;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.OrdenEstadoCervezaEnum;
import guru.sfg.beer.order.service.domain.OrdenEventoCervezaEnum;
import guru.sfg.beer.order.service.services.CervezaOrdenManagerImpl;
import guru.sfg.brewery.model.events.UbicacionFallaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AsignarFallaAction implements Action<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> {

   private final JmsTemplate jmsTemplate;

   @Override
   public void execute(StateContext<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> stateContext) {
      String cervezaOrdenId = (String) stateContext.getMessage().getHeaders().get(CervezaOrdenManagerImpl.ORDEN_ID_HEADER);

      jmsTemplate.convertAndSend(JmsConfig.ASIGNAR_FALLA_QUEUE, UbicacionFallaEvent.builder()
         .ordenId(UUID.fromString(cervezaOrdenId))
            .build());

      log.debug("Envio mensaje ubicacion con falla a la queue para la orden Id: " + cervezaOrdenId);
   }
}