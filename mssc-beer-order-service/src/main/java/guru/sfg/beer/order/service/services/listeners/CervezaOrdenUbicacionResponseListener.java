package guru.sfg.beer.order.service.services.listeners;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.services.CervezaOrdenManager;
import guru.sfg.brewery.model.events.AsignarOrdenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CervezaOrdenUbicacionResponseListener {

   private final CervezaOrdenManager cervezaOrdenManager;

   @JmsListener(destination = JmsConfig.ASIGNAR_ORDEN_RESPONSE_QUEUE)
   public void listen(AsignarOrdenResponse response) {
      if (!response.getAsignacionError() && !response.getPendienteInventario()) {
         // Ubicacion normal
         cervezaOrdenManager.cervezaOrdenUbicacionAprobado(response.getCervezaOrden());
      } else if (!response.getAsignacionError() && response.getPendienteInventario()) {
         // Inventario pendiente
         cervezaOrdenManager.cervezaOrdenUbicacionPendienteInventario(response.getCervezaOrden());
      } else if (response.getAsignacionError()) {
         //Ubicacion error
         cervezaOrdenManager.cervezaOrdenUbicacionError(response.getCervezaOrden());
      }

   }

}
