package com.stereo.msscssm.services;

import com.stereo.msscssm.domain.Pago;
import com.stereo.msscssm.domain.PagoEstado;
import com.stereo.msscssm.domain.PagoEvento;
import com.stereo.msscssm.repositorio.PagoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PagoEstadoCambioInterceptor extends StateMachineInterceptorAdapter<PagoEstado, PagoEvento> {

   private final PagoRepositorio pagoRepositorio;

   @Override
   public void preStateChange(State<PagoEstado, PagoEvento> state, Message<PagoEvento> message, Transition<PagoEstado, PagoEvento> transition,
                              StateMachine<PagoEstado, PagoEvento> stateMachine) {

      Optional.ofNullable(message).ifPresent(msg -> {
         Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(PagoServiceImpl.PAGO_ID_CABECERA, -1L)))
                 .ifPresent(pagoId -> {
                    Pago pago = pagoRepositorio.getOne(pagoId);
                    pago.setEstado(state.getId());
                    pagoRepositorio.save(pago);
                 });
      });
   }
}
