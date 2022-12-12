package com.stereo.sfjms.listener;

import com.stereo.sfjms.config.JmsConfig;
import com.stereo.sfjms.model.HolaMundoMensaje;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Component        // comentar para docker
public class HolaMundoListener {

   private final JmsTemplate jmsTemplate;

   @JmsListener(destination = JmsConfig.MI_QUEUE)
   public void listen(@Payload HolaMundoMensaje holaMundoMensaje,
                      @Headers MessageHeaders cabeceras, Message mensaje) {

      System.out.println("Tengo un mensaje");
      System.out.println(holaMundoMensaje);

      //throw new RuntimeException("foo");

   }


   @JmsListener(destination = JmsConfig.MI_ENVIAR_RECIBIR)
   public void listenForHello(@Payload HolaMundoMensaje holaMundoMensaje,
                      @Headers MessageHeaders cabeceras, Message mensaje) throws JMSException {

      HolaMundoMensaje payloadMensaje = HolaMundoMensaje
         .builder()
         .id(UUID.randomUUID())
         .mensaje("Mundo!!!!")
         .build();

      //jmsTemplate.convertAndSend((Destination) mensaje.getHeaders().get("jms_replyTo"), "Lo tienes");

      jmsTemplate.convertAndSend(mensaje.getJMSReplyTo(), payloadMensaje);

   }

}
