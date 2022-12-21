package com.stereo.msscssm.services;

import com.stereo.msscssm.domain.Pago;
import com.stereo.msscssm.domain.PagoEstado;
import com.stereo.msscssm.domain.PagoEvento;
import com.stereo.msscssm.repositorio.PagoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PagoServiceImpl implements PagoService {

   public static final String PAGO_ID_CABECERA = "pago_id";
   private final PagoRepositorio pagoRepositorio;
   private final StateMachineFactory<PagoEstado, PagoEvento> stateMachineFactory;
   private final PagoEstadoCambioInterceptor pagoEstadoCambioInterceptor;

   @Override
   public Pago nuevoPago(Pago pago) {
      pago.setEstado(PagoEstado.NUEVO);
      return pagoRepositorio.save(pago);
   }

   @Transactional
   @Override
   public StateMachine<PagoEstado, PagoEvento> preAutorizacion(Long pagoId) {
      StateMachine<PagoEstado, PagoEvento> sm = build(pagoId);
      enviarEvento(pagoId, sm, PagoEvento.PRE_AUTORIZAR);
      return sm;
   }

   @Transactional
   @Override
   public StateMachine<PagoEstado, PagoEvento> pagoAutorizacion(Long pagoId) {
      StateMachine<PagoEstado, PagoEvento> sm = build(pagoId);
      enviarEvento(pagoId, sm, PagoEvento.AUTORIZAR_APROBADO);
      return sm;
   }

   @Transactional
   @Override
   public StateMachine<PagoEstado, PagoEvento> AutorizacionRechazada(Long pagoId) {
      StateMachine<PagoEstado, PagoEvento> sm = build(pagoId);
      enviarEvento(pagoId, sm, PagoEvento.AUTORIZAR_RECHAZADO);
      return sm;
   }

   private void enviarEvento(Long pagoId, StateMachine<PagoEstado, PagoEvento> sm, PagoEvento evento) {
      Message mensaje = MessageBuilder.withPayload(evento)
                                      .setHeader(PAGO_ID_CABECERA, pagoId)
                                      .build();
      sm.sendEvent(mensaje);
   }

   private StateMachine<PagoEstado, PagoEvento> build(Long pagoId) {
      Pago pago = pagoRepositorio.getOne(pagoId);

      StateMachine<PagoEstado, PagoEvento> sm = stateMachineFactory.getStateMachine(Long.toString(pago.getId()));

      sm.stop();

      sm.getStateMachineAccessor().doWithAllRegions( sma -> {
         sma.addStateMachineInterceptor(pagoEstadoCambioInterceptor);
         sma.resetStateMachine(new DefaultStateMachineContext<>(pago.getEstado(), null, null, null));
      });

      sm.start();
      return sm;
   }
}
