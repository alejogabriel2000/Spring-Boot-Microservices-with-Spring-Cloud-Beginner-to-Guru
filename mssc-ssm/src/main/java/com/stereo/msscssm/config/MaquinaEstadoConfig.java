package com.stereo.msscssm.config;

import com.stereo.msscssm.domain.PagoEstado;
import com.stereo.msscssm.domain.PagoEvento;
import com.stereo.msscssm.services.PagoServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@EnableStateMachineFactory
@Configuration
public class MaquinaEstadoConfig extends StateMachineConfigurerAdapter<PagoEstado, PagoEvento> {

   private final Action<PagoEstado, PagoEvento> preAutorizacionAction;
   private final Action<PagoEstado, PagoEvento> autorizacionAction;
   private final Guard<PagoEstado, PagoEvento> pagoIdGuard;
   private final Action<PagoEstado, PagoEvento> preAutorizacionAprobadaAction;
   private final Action<PagoEstado, PagoEvento> preAutorizacionRechazadaAction;
   private final Action<PagoEstado, PagoEvento> autorizacionAprobadaAction;
   private final Action<PagoEstado, PagoEvento> autorizacionRechazadaAction;





   @Override
   public void configure(StateMachineStateConfigurer<PagoEstado, PagoEvento> estados) throws Exception {
      estados.withStates()
         .initial(PagoEstado.NUEVO)
         .states(EnumSet.allOf(PagoEstado.class))
         .end(PagoEstado.AUTORIZACION)
         .end(PagoEstado.PRE_AUTORIZACION_ERROR)
         .end(PagoEstado.AUTORIZACION_ERROR);
   }

   @Override
   public void configure(StateMachineTransitionConfigurer<PagoEstado, PagoEvento> transiciones) throws Exception {

      transiciones.withExternal().source(PagoEstado.NUEVO).target(PagoEstado.NUEVO).event(PagoEvento.PRE_AUTORIZAR)
                  .action(preAutorizacionAction).guard(pagoIdGuard)
         .and()
         .withExternal().source(PagoEstado.NUEVO).target(PagoEstado.PRE_AUTORIZACION).event(PagoEvento.PRE_AUTORIZAR_APROBADO)
         .action(preAutorizacionAprobadaAction)
         .and()
         .withExternal().source(PagoEstado.NUEVO).target(PagoEstado.PRE_AUTORIZACION_ERROR).event(PagoEvento.PRE_AUTORIZAR_RECHAZADO)
         .action(preAutorizacionRechazadaAction)
         .and()
         .withExternal().source(PagoEstado.PRE_AUTORIZACION).target(PagoEstado.PRE_AUTORIZACION).event(PagoEvento.AUTORIZAR)
                  .action(autorizacionAction)
         .and()
         .withExternal().source(PagoEstado.PRE_AUTORIZACION).target(PagoEstado.AUTORIZACION).event(PagoEvento.AUTORIZAR_APROBADO)
         .action(autorizacionAprobadaAction)
         .and()
         .withExternal().source(PagoEstado.PRE_AUTORIZACION).target(PagoEstado.AUTORIZACION_ERROR).event(PagoEvento.AUTORIZAR_RECHAZADO)
         .action(autorizacionRechazadaAction);
   }

   @Override
   public void configure(StateMachineConfigurationConfigurer<PagoEstado, PagoEvento> config) throws Exception {

      StateMachineListenerAdapter<PagoEstado, PagoEvento> adapter = new StateMachineListenerAdapter<>(){
         @Override
         public void stateChanged(State<PagoEstado, PagoEvento> from, State<PagoEstado, PagoEvento> to) {
            log.info(String.format("Cambio de estado(desde: %s, a: %s)", from, to));
         }
      };
      config.withConfiguration().listener(adapter);
   }

/*   public Guard<PagoEstado, PagoEvento> pagoIdGuard() {
      return  context -> {
         return context.getMessageHeader(PagoServiceImpl.PAGO_ID_CABECERA) != null;
      };
   }

   public Action<PagoEstado, PagoEvento> preAutorizacionAction() {
      return context -> {
         System.out.println("PreAutorizacion fue llamado");
         if (new Random().nextInt(10) < 8) {
            System.out.println("Pre autorizacion Aprobado");
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(PagoEvento.PRE_AUTORIZAR_APROBADO)
                                                              .setHeader(PagoServiceImpl.PAGO_ID_CABECERA,
                                                                         context.getMessageHeader(PagoServiceImpl.PAGO_ID_CABECERA)).build());
         } else {
            System.out.println("Pre Autorizacion Rechazada, no hay credito!!!!!");
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(PagoEvento.PRE_AUTORIZAR_RECHAZADO)
                                                              .setHeader(PagoServiceImpl.PAGO_ID_CABECERA,
                                                                         context.getMessageHeader(PagoServiceImpl.PAGO_ID_CABECERA)).build());
         }
      };
   }


   public Action<PagoEstado, PagoEvento> AutorizacionAction() {
      return context -> {
         System.out.println("Autorizacion fue llamado");
         if (new Random().nextInt(10) < 8) {
            System.out.println("Autorizacion Aprobado");
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(PagoEvento.AUTORIZAR_APROBADO)
                                                              .setHeader(PagoServiceImpl.PAGO_ID_CABECERA,
                                                                         context.getMessageHeader(PagoServiceImpl.PAGO_ID_CABECERA)).build());
         } else {
            System.out.println("Autorizacion Rechazada, no hay credito!!!!!");
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(PagoEvento.AUTORIZAR_RECHAZADO)
                                                              .setHeader(PagoServiceImpl.PAGO_ID_CABECERA,
                                                                         context.getMessageHeader(PagoServiceImpl.PAGO_ID_CABECERA)).build());
         }
      };
   }*/
}