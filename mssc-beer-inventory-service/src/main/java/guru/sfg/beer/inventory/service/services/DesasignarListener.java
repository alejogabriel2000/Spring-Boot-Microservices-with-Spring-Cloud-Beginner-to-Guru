package guru.sfg.beer.inventory.service.services;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.brewery.model.events.DesasignarOrdenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DesasignarListener {

   private final AsignacionService asignacionService;

   @JmsListener(destination = JmsConfig.ASIGNAR_ORDEN_RESPONSE_QUEUE)
   public void listen(DesasignarOrdenRequest request) {
      asignacionService.desasignarOrden(request.getBeerOrderDto());
   }
}