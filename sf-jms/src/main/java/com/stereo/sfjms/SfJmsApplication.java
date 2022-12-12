package com.stereo.sfjms;

import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.core.server.ActiveMQServers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SfJmsApplication{

   public static void main(String[] args) throws Exception {

      // COMENTAR AL USAR DOCKER
      ActiveMQServer servidor = ActiveMQServers.newActiveMQServer(new ConfigurationImpl()
                                                                     .setPersistenceEnabled(false)
                                                                     .setJournalDirectory("target/data/journal")
                                                                     .setSecurityEnabled(false)
                                                                     .addAcceptorConfiguration("invm", "vm://0"));

      servidor.start();



      SpringApplication.run(SfJmsApplication.class, args);
   }

}
