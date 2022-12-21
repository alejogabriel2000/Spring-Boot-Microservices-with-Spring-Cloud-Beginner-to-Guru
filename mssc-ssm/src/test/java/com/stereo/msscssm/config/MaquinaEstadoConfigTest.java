package com.stereo.msscssm.config;

import com.stereo.msscssm.domain.PagoEstado;
import com.stereo.msscssm.domain.PagoEvento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;


@SpringBootTest
class MaquinaEstadoConfigTest {

   @Autowired
   StateMachineFactory<PagoEstado, PagoEvento> factory;

   @Test
   void testNuevoMaquinaEstado() {

      StateMachine<PagoEstado, PagoEvento> sm = factory.getStateMachine(UUID.randomUUID());

      sm.start();

      System.out.println(sm.getState().toString());

      sm.sendEvent(PagoEvento.PRE_AUTORIZAR);

      System.out.println(sm.getState().toString());

      sm.sendEvent(PagoEvento.PRE_AUTORIZAR_APROBADO);

      System.out.println(sm.getState().toString());

      // No da error, se queda en el mismo estado ya que no hay una transicion
      sm.sendEvent(PagoEvento.PRE_AUTORIZAR_RECHAZADO);
      System.out.println(sm.getState().toString());

   }
}