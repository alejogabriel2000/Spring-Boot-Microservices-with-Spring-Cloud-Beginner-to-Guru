package guru.sfg.beer.order.service.services.testcomponents;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.brewery.model.events.ValidarOrdenRequest;
import guru.sfg.brewery.model.events.ValidarOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CervezaOrdenValidacionListener {

   private final JmsTemplate jmsTemplate;

   @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
   public void listar(Message mensaje) {

      boolean esValido = true;
      ValidarOrdenRequest request = (ValidarOrdenRequest) mensaje.getPayload();

      if (request.getCervezaOrden().getCustomerRef() != null && request.getCervezaOrden().getCustomerRef().equals("fallo-validacion")) {
         esValido = false;
      }

      jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE,
                                 ValidarOrderResponse.builder()
                                                     .esValido(esValido)
                                                     .ordenId(request.getCervezaOrden().getId())
                                                     .build());
   }
}