package guru.springframework.brewery.events;

import org.springframework.context.ApplicationEvent;

import guru.springframework.brewery.domain.CervezaOrden;
import guru.springframework.brewery.web.model.OrdenEstadoEnum;

public class CervezaOrdenEstadoChangeEvent extends ApplicationEvent {

   private final OrdenEstadoEnum estadoPrevio;

   public CervezaOrdenEstadoChangeEvent(CervezaOrden source, OrdenEstadoEnum estadoPrevio) {
      super(source);
      this.estadoPrevio = estadoPrevio;
   }

   public OrdenEstadoEnum getPreviousStatus() {
      return estadoPrevio;
   }

   public CervezaOrden getCervezaOrden(){
      return (CervezaOrden) this.source;
   }
}
