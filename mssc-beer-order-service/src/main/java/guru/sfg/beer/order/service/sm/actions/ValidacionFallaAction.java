package guru.sfg.beer.order.service.sm.actions;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import guru.sfg.beer.order.service.domain.OrdenEstadoCervezaEnum;
import guru.sfg.beer.order.service.domain.OrdenEventoCervezaEnum;
import guru.sfg.beer.order.service.services.CervezaOrdenManagerImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ValidacionFallaAction implements Action<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> {

   @Override
   public void execute(StateContext<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> stateContext) {
      String cervezaOrdenId = (String) stateContext.getMessage().getHeaders().get(CervezaOrdenManagerImpl.ORDEN_ID_HEADER);
      log.error("Compensating Transaction... Validacion Fallo: " + cervezaOrdenId);
   }
}