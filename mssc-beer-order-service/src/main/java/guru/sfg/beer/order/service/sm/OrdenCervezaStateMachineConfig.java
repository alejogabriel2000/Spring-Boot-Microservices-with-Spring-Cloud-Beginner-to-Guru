package guru.sfg.beer.order.service.sm;

import guru.sfg.beer.order.service.domain.OrdenEstadoCervezaEnum;
import guru.sfg.beer.order.service.domain.OrdenEventoCervezaEnum;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@RequiredArgsConstructor
@Configuration
@EnableStateMachineFactory
public class OrdenCervezaStateMachineConfig extends StateMachineConfigurerAdapter<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> {

   private final Action<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> validacionOrdenAction;
   private final Action<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> asignarOrdenAction;
   private final Action<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> validacionFallaAction;

   @Override
   public void configure(StateMachineStateConfigurer<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> estados) throws Exception {

      estados.withStates()
         .initial(OrdenEstadoCervezaEnum.NUEVO)
         .states(EnumSet.allOf(OrdenEstadoCervezaEnum.class))
         .end(OrdenEstadoCervezaEnum.RETIRADO)
         .end(OrdenEstadoCervezaEnum.ENTREGADO)
         .end(OrdenEstadoCervezaEnum.ENTREGA_EXCEPCION)
         .end(OrdenEstadoCervezaEnum.VALIDACION_EXCEPCION)
         .end(OrdenEstadoCervezaEnum.ASIGNACION_EXCEPCION);
   }

   @Override
   public void configure(StateMachineTransitionConfigurer<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> transiciones) throws Exception {
      transiciones.withExternal()
            .source(OrdenEstadoCervezaEnum.NUEVO).target(OrdenEstadoCervezaEnum.VALIDACION_PENDIENTE)
            .event(OrdenEventoCervezaEnum.VALIDAR_PEDIDO)
            .action(validacionOrdenAction)
         .and().withExternal()
            .source(OrdenEstadoCervezaEnum.VALIDACION_PENDIENTE).target(OrdenEstadoCervezaEnum.VALIDADO)
            .event(OrdenEventoCervezaEnum.VALIDACION_APROBADA)
         .and().withExternal()
            .source(OrdenEstadoCervezaEnum.VALIDACION_PENDIENTE).target(OrdenEstadoCervezaEnum.VALIDACION_EXCEPCION)
            .event(OrdenEventoCervezaEnum.VALIDACION_FALLIDA)
            .action(validacionFallaAction)
         .and().withExternal()
             .source(OrdenEstadoCervezaEnum.VALIDADO).target(OrdenEstadoCervezaEnum.ASIGNADO_PENDIENTE)
             .event(OrdenEventoCervezaEnum.ASIGNAR_PEDIDO)
             .action(asignarOrdenAction)
         .and().withExternal()
             .source(OrdenEstadoCervezaEnum.ASIGNADO_PENDIENTE).target(OrdenEstadoCervezaEnum.ASIGNADO)
             .event(OrdenEventoCervezaEnum.ASIGNACION_EXITOSA)
         .and().withExternal()
              .source(OrdenEstadoCervezaEnum.ASIGNADO_PENDIENTE).target(OrdenEstadoCervezaEnum.ASIGNACION_EXCEPCION)
              .event(OrdenEventoCervezaEnum.ASIGNACION_FALLIDA)
         .and().withExternal()
              .source(OrdenEstadoCervezaEnum.ASIGNADO_PENDIENTE).target(OrdenEstadoCervezaEnum.INVENTARIO_PENDIENTE)
              .event(OrdenEventoCervezaEnum.ASIGNACION_SIN_INVENTARIO)
         .and().withExternal()
            .source(OrdenEstadoCervezaEnum.ASIGNADO).target(OrdenEstadoCervezaEnum.RETIRADO)
            .event(OrdenEventoCervezaEnum.PEDIDO_CERVEZA_ENTREGADO);
   }
}