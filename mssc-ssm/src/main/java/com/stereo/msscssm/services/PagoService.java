package com.stereo.msscssm.services;

import com.stereo.msscssm.domain.Pago;
import com.stereo.msscssm.domain.PagoEstado;
import com.stereo.msscssm.domain.PagoEvento;
import org.springframework.statemachine.StateMachine;

public interface PagoService {

   Pago nuevoPago(Pago pago);

   StateMachine<PagoEstado, PagoEvento> preAutorizacion(Long pagoId);

   StateMachine<PagoEstado, PagoEvento> pagoAutorizacion(Long pagoId);

   StateMachine<PagoEstado, PagoEvento> AutorizacionRechazada(Long pagoId);
}
