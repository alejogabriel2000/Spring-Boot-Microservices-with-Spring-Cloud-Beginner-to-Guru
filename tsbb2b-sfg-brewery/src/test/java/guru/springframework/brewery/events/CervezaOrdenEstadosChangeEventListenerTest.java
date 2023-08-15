package guru.springframework.brewery.events;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.client.RestTemplateBuilder;

import com.github.jenspiegsa.wiremockextension.Managed;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;

import guru.springframework.brewery.domain.CervezaOrden;
import guru.springframework.brewery.web.model.OrdenEstadoEnum;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


@ExtendWith(WireMockExtension.class)
class CervezaOrdenEstadosChangeEventListenerTest {

   @Managed
   WireMockServer wireMockServer = with(wireMockConfig().dynamicPort());

   CervezaOrdenEstadosChangeEventListener listener;

   @BeforeEach
   void setUp() {
      RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
      listener = new CervezaOrdenEstadosChangeEventListener(restTemplateBuilder);
   }

   @Test
   void listen() {

      wireMockServer.stubFor(post("/update").willReturn(ok()));

      CervezaOrden cervezaOrden = CervezaOrden.builder()
         .ordenEstado(OrdenEstadoEnum.NUEVO)
         .ordenEstadoCallbackUrl("http://localhost:" + wireMockServer.port() + "/update")
         .creacionFecha(Timestamp.valueOf(LocalDateTime.now()))
         .build();

      CervezaOrdenEstadoChangeEvent event = new CervezaOrdenEstadoChangeEvent(cervezaOrden, OrdenEstadoEnum.NUEVO);
      listener.listen(event);

      verify(12,postRequestedFor(urlEqualTo("/update")));
   }
}