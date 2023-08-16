package guru.sfg.beer.order.service.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.domain.OrdenEstadoCervezaEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import guru.sfg.beer.order.service.services.cerveza.CervezaServiceImpl;
import guru.sfg.brewery.model.CervezaDTO;
import guru.sfg.brewery.model.CervezaListaPaginada;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.jgroups.util.Util.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(WireMockExtension.class)
@SpringBootTest
public class CervezaOrdenManagerImplIT {

   @Autowired
   CervezaOrdenManager cervezaOrdenManager;

   @Autowired
   BeerOrderRepository beerOrderRepository;

   @Autowired
   CustomerRepository customerRepository;

   @Autowired
   ObjectMapper objectMapper;

   @Autowired
   WireMockServer wireMockServer;

   Customer testCustomer;

   UUID cervezaID = UUID.randomUUID();

   @TestConfiguration
   static class RestTemplateBuilderProvider {
      @Bean(destroyMethod = "stop")
      public WireMockServer wireMockServer() {
         WireMockServer servidor = with(wireMockConfig().port(8083));
         servidor.start();
         return servidor;
      }
   }

   @BeforeEach
   void setUp() {
      testCustomer = customerRepository.save(Customer.builder()
              .customerName("Cliente test")
              .build());
   }

   public BeerOrder crearOrdenCerveza() {
      BeerOrder cervezaOrden = BeerOrder.builder()
         .customer(testCustomer)
         .build();

      Set<BeerOrderLine> lineas = new HashSet<>();
      lineas.add(BeerOrderLine.builder()
                    .beerId(cervezaID)
                    .upc("12345")
                    .orderQuantity(1)
                    .beerOrder(cervezaOrden)
                    .build());
      cervezaOrden.setBeerOrderLines(lineas);
      return cervezaOrden;
   }

   @Test
   public void testNuevoAUbicacion() throws JsonProcessingException, InterruptedException {
      CervezaDTO cervezaDTO = CervezaDTO.builder().id(cervezaID).upc("12345").build();
      //CervezaListaPaginada lista = new CervezaListaPaginada(Arrays.asList(cervezaDTO));
      wireMockServer.stubFor(get(CervezaServiceImpl.CERVEZA_UPC_PATH_V1 + "12345")
                                .willReturn(okJson(objectMapper.writeValueAsString(cervezaDTO))));
      BeerOrder cervezaOrden = crearOrdenCerveza();
      BeerOrder cervezaOrdenGuardada = cervezaOrdenManager.nuevaOrdenCerveza(cervezaOrden);

      Thread.sleep(5000);

      BeerOrder cervezaOrdenGuardada2 = beerOrderRepository.findById(cervezaOrden.getId()).get();
      assertNotNull(cervezaOrdenGuardada);
      assertEquals(OrdenEstadoCervezaEnum.ASIGNADO, cervezaOrdenGuardada2.getOrderStatus());
   }
}