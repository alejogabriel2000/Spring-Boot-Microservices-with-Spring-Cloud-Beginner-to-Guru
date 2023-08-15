package guru.springframework.brewery.events;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import guru.springframework.brewery.web.mappers.FechaMapper;
import guru.springframework.brewery.web.model.OrdenEstadoActualizacion;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CervezaOrdenEstadosChangeEventListener {

   RestTemplate restTemplate;
   FechaMapper dateMapper = new FechaMapper();

   public CervezaOrdenEstadosChangeEventListener(RestTemplateBuilder restTemplateBuilder) {
      this.restTemplate = restTemplateBuilder.build();
   }

   @Async
   @EventListener
   public void listen(CervezaOrdenEstadoChangeEvent event){
      System.out.println("I got an order status change event");
      System.out.println(event);

     OrdenEstadoActualizacion update = OrdenEstadoActualizacion.builder()
                                                                .id(event.getCervezaOrden().getId())
                                                                .ordenId(event.getCervezaOrden().getId())
                                                                .version(event.getCervezaOrden().getVersion() != null ? event.getCervezaOrden().getVersion().intValue() : null)
                                                                .creacionFecha(dateMapper.asOffsetDateTime(event.getCervezaOrden().getCreacionFecha()))
                                                                .ultimaModificacionFecha(dateMapper.asOffsetDateTime(event.getCervezaOrden().getUltimaModificacionFecha()))
                                                                .ordenEstado(event.getCervezaOrden().getOrdenEstado().toString())
                                                                .clienteRef(event.getCervezaOrden().getClienteRef())
                                                                .build();

      try{
         log.debug("Posteo para respuesta url");
         restTemplate.postForObject(event.getCervezaOrden().getOrdenEstadoCallbackUrl(), update, String.class);
      } catch (Throwable t){
         log.error("Error al realizar la llamada para la orden: " + event.getCervezaOrden().getId(), t);
      }
   }


}
