package com.stereo.msscssm.config.guards;

import com.stereo.msscssm.domain.PagoEstado;
import com.stereo.msscssm.domain.PagoEvento;
import com.stereo.msscssm.services.PagoServiceImpl;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class PagoIdGuard implements Guard<PagoEstado, PagoEvento> {

   @Override
   public boolean evaluate(StateContext<PagoEstado, PagoEvento> stateContext) {
      return stateContext.getMessageHeader(PagoServiceImpl.PAGO_ID_CABECERA) != null;
   }
}
