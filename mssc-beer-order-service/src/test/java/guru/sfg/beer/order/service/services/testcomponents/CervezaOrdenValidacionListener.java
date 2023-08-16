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

      ValidarOrdenRequest request = (ValidarOrdenRequest) mensaje.getPayload();

      System.out.println("###########################");

      jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE,
                                 ValidarOrderResponse.builder()
                                                     .esValido(true)
                                                     .ordenId(request.getCervezaOrden().getId())
                                                     .build());
   }
}
