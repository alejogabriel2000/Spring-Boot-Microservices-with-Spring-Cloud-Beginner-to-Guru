package com.stereo.msscssm.actions;

import com.stereo.msscssm.domain.PagoEstado;
import com.stereo.msscssm.domain.PagoEvento;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class PreAutorizacionAprobadaAction implements Action<PagoEstado, PagoEvento> {

   @Override
   public void execute(StateContext<PagoEstado, PagoEvento> stateContext) {
      System.out.println("Enviando Notificacion de PreAutorizacionAprobada");

   }
}
