package com.stereo.sfjms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {

   public static final String MI_QUEUE = "mi-hola-mundo";
   public static final String MI_ENVIAR_RECIBIR = "responderDeVueltaAMi";

   @Bean
   public MessageConverter convertidosMensaje() {
      MappingJackson2MessageConverter convertidor = new MappingJackson2MessageConverter();
      convertidor.setTargetType(MessageType.TEXT);
      convertidor.setTypeIdPropertyName("_type");

      return convertidor;
   }
}
