package guru.sfg.beer.order.service.sm;

import guru.sfg.beer.order.service.domain.OrdenEstadoCervezaEnum;
import guru.sfg.beer.order.service.domain.OrdenEventoCervezaEnum;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

public class OrdenCervezaStateMachineConfig extends StateMachineConfigurerAdapter<OrdenEstadoCervezaEnum, OrdenEventoCervezaEnum> {

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
            .source(OrdenEstadoCervezaEnum.NUEVO).target(OrdenEstadoCervezaEnum.NUEVO)
            .event(OrdenEventoCervezaEnum.VALIDAR_PEDIDO)
                  // TODO AGREGAR ACCION DE VALIDACION
         .and().withExternal()
            .source(OrdenEstadoCervezaEnum.NUEVO).target(OrdenEstadoCervezaEnum.VALIDADO)
            .event(OrdenEventoCervezaEnum.VALIDACION_APROBADA)
         .and().withExternal()
            .source(OrdenEstadoCervezaEnum.NUEVO).target(OrdenEstadoCervezaEnum.VALIDACION_EXCEPCION)
            .event(OrdenEventoCervezaEnum.VALIDACION_FALLIDA);



   }
}
