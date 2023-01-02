package guru.sfg.beer.inventory.service.services;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.brewery.model.events.AsignarOrdenRequest;
import guru.sfg.brewery.model.events.AsignarOrdenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AsignarListener {

   private final AsignacionService asignacionService;
   private final JmsTemplate jmsTemplate;

   @JmsListener(destination = JmsConfig.ASIGNAR_ORDEN_QUEUE)
   public void listen(AsignarOrdenRequest asignarOrdenRequest) {

      AsignarOrdenResponse.AsignarOrdenResponseBuilder builder = AsignarOrdenResponse.builder();
      builder.cervezaOrdenDto(asignarOrdenRequest.getBeerOrderDto());

      try {
         Boolean asignacionResultado = asignacionService.asignarOrden(asignarOrdenRequest.getBeerOrderDto());
         if (asignacionResultado) {
            builder.pendienteInventario(false);
         } else {
            builder.pendienteInventario(true);
         }
      } catch (Exception ex) {
         log.error("Asignacion con error para la orden ID: " + asignarOrdenRequest.getBeerOrderDto().getId());
         builder.asignacionError(true);
      }

      jmsTemplate.convertAndSend(JmsConfig.ASIGNAR_ORDEN_RESPONSE_QUEUE, builder.build());
   }
}
