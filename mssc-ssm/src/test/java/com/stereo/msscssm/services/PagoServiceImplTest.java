package com.stereo.msscssm.services;

import com.stereo.msscssm.domain.Pago;
import com.stereo.msscssm.domain.PagoEstado;
import com.stereo.msscssm.domain.PagoEvento;
import com.stereo.msscssm.repositorio.PagoRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PagoServiceImplTest {

   @Autowired
   PagoService pagoService;

   @Autowired
   PagoRepositorio pagoRepositorio;

   Pago pago;

   @BeforeEach
   void setUp() {
      pago = Pago.builder().monto(new BigDecimal("12.99")).build();
   }

   @Transactional
   @Test
   void preAutorizacion() {
      Pago pagoGuardado = pagoService.nuevoPago(pago);

      System.out.println("Debería ser NUEVO");
      System.out.println(pagoGuardado.getEstado());

      StateMachine<PagoEstado, PagoEvento> sm = pagoService.preAutorizacion(pagoGuardado.getId());

      Pago pagoPreAutorizado = pagoRepositorio.getOne(pagoGuardado.getId());
      System.out.println("Debería ser PRE_AUTORIZACION o PRE_AUTORIZACION_ERROR");
      System.out.println(sm.getState().getId());
      System.out.println(pagoPreAutorizado);
   }



   @Transactional
   @RepeatedTest(10)
   void autorizacion() {
      Pago pagoGuardado = pagoService.nuevoPago(pago);
      StateMachine<PagoEstado, PagoEvento> preAuthSM = pagoService.preAutorizacion(pagoGuardado.getId());

      if (preAuthSM.getState().getId() == PagoEstado.PRE_AUTORIZACION) {
         System.out.println("El pago es PRE_AUTORIZADO");

         StateMachine<PagoEstado, PagoEvento> authSM = pagoService.pagoAutorizacion(pagoGuardado.getId());
         System.out.println("Resultado de autorizacion: " + authSM.getState().getId());
      } else {
         System.out.println("El pago fallo PRE_AUTORIZACION....");
      }

   }
}