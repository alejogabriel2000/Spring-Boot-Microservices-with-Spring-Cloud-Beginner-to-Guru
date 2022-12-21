package com.stereo.msscssm.actions;

import com.stereo.msscssm.domain.PagoEstado;
import com.stereo.msscssm.domain.PagoEvento;
import com.stereo.msscssm.services.PagoServiceImpl;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AutorizacionAction implements Action<PagoEstado, PagoEvento> {

   @Override
   public void execute(StateContext<PagoEstado, PagoEvento> stateContext) {
      System.out.println("Autorizacion fue llamado");
      if (new Random().nextInt(10) < 8) {
         System.out.println("Autorizacion Aprobado");
         stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(PagoEvento.AUTORIZAR_APROBADO)
                                                           .setHeader(PagoServiceImpl.PAGO_ID_CABECERA,
                                                                      stateContext.getMessageHeader(PagoServiceImpl.PAGO_ID_CABECERA)).build());
      } else {
         System.out.println("Autorizacion Rechazada, no hay credito!!!!!");
         stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(PagoEvento.AUTORIZAR_RECHAZADO)
                                                           .setHeader(PagoServiceImpl.PAGO_ID_CABECERA,
                                                                      stateContext.getMessageHeader(PagoServiceImpl.PAGO_ID_CABECERA)).build());
      }

   }
}
