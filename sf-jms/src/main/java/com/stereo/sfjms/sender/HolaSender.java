package com.stereo.sfjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stereo.sfjms.config.JmsConfig;
import com.stereo.sfjms.model.HolaMundoMensaje;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HolaSender {

   private final JmsTemplate jmsTemplate;
   private final ObjectMapper objectMapper;

   @Scheduled(fixedRate = 2000)
   public void enviarMensaje() {
      System.out.println("Enviando un mensaje");

      HolaMundoMensaje mensaje = HolaMundoMensaje
         .builder()
         .id(UUID.randomUUID())
         .mensaje("Hola Mundo")
         .build();

      jmsTemplate.convertAndSend(JmsConfig.MI_QUEUE, mensaje);

      System.out.println("Mensaje Enviado");
   }


   @Scheduled(fixedRate = 2000)
   public void enviarYRecibirMensaje() throws JMSException {
      HolaMundoMensaje mensaje = HolaMundoMensaje
         .builder()
         .id(UUID.randomUUID())
         .mensaje("Hola")
         .build();

      Message receivedMensaje = jmsTemplate.sendAndReceive(JmsConfig.MI_ENVIAR_RECIBIR, new MessageCreator() {
         @Override
         public Message createMessage(Session session) throws JMSException {
            Message mensajeHola = null;
            try {
               mensajeHola = session.createTextMessage(objectMapper.writeValueAsString(mensaje));
               mensajeHola.setStringProperty("_type", "com.stereo.sfjms.model.HolaMundoMensaje");
               System.out.println("Enviando Hola");
               return  mensajeHola;
            } catch (JsonProcessingException e) {
               e.printStackTrace();
               throw new JMSException("Exploto");
            }
         }
      });
      System.out.println(receivedMensaje.getBody(String.class));
   }
}
