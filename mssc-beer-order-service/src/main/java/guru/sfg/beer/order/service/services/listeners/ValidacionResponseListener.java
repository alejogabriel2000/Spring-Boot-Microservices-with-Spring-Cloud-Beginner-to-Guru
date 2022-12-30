package guru.sfg.beer.order.service.services.listeners;

import java.util.UUID;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.services.CervezaOrdenManager;
import guru.sfg.brewery.model.events.ValidarOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidacionResponseListener {

   private final CervezaOrdenManager cervezaOrdenManager;

   @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE)
   public void listen(ValidarOrderResponse response) {
      final UUID cervezaOrdenId = response.getOrdenId();

      log.debug("Validacion del resultado para la ordenId: " + cervezaOrdenId);

      cervezaOrdenManager.procesarValidacionResponse(cervezaOrdenId, response.getEsValido());

   }

}
