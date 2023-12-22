package guru.sfg.beer.order.service.services.testcomponents;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.brewery.model.events.AsignarOrdenRequest;
import guru.sfg.brewery.model.events.AsignarOrdenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CervezaOrderUbicacionListener {

   private final JmsTemplate jmsTemplate;

   @JmsListener(destination = JmsConfig.ASIGNAR_ORDEN_QUEUE)
   public void listen(Message msg) {
      AsignarOrdenRequest request = (AsignarOrdenRequest) msg.getPayload();
      boolean inventarioPendiente = false;
      boolean asignacionError = false;
      boolean envioRespuesta = true;

      if (request.getCervezaOrden().getCustomerRef() != null) {
         if (request.getCervezaOrden().getCustomerRef().equals("fallo-ubicacion")) {
            asignacionError = true;
         } else if (request.getCervezaOrden().getCustomerRef().equals("no-se-ubico")) {
            envioRespuesta = false;
         } else if (request.getCervezaOrden().getCustomerRef().equals("ubicacion-parcial")) {
            inventarioPendiente = true;
         }
      }

      boolean finalInventarioPendiente = inventarioPendiente;
      request.getCervezaOrden().getBeerOrderLines().forEach(cervezaOrdenLineDTO -> {
         if(finalInventarioPendiente) {
            cervezaOrdenLineDTO.setQuantityAllocated(cervezaOrdenLineDTO.getOrderQuantity() - 1);
         } else {
            cervezaOrdenLineDTO.setQuantityAllocated(cervezaOrdenLineDTO.getOrderQuantity());
         }
      });

      if (envioRespuesta) {
         jmsTemplate.convertAndSend(JmsConfig.ASIGNAR_ORDEN_RESPONSE_QUEUE,
                                    AsignarOrdenResponse.builder()
                                                        .cervezaOrden(request.getCervezaOrden())
                                                        .pendienteInventario(inventarioPendiente)
                                                        .asignacionError(asignacionError)
                                                        .build());
      }
   }
}