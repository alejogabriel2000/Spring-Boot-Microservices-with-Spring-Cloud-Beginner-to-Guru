package guru.sfg.beer.order.service.services;

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

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.awaitility.Awaitility.await;
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
   void testNuevoAUbicacion() throws JsonProcessingException {
      CervezaDTO cervezaDTO = CervezaDTO.builder().id(cervezaID).upc("12345").build();
      wireMockServer.stubFor(get(CervezaServiceImpl.CERVEZA_UPC_PATH_V1 + "12345")
                                .willReturn(okJson(objectMapper.writeValueAsString(cervezaDTO))));
      BeerOrder cervezaOrden = crearOrdenCerveza();
      BeerOrder cervezaOrdenGuardada = cervezaOrdenManager.nuevaOrdenCerveza(cervezaOrden);

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).get();
         assertEquals(OrdenEstadoCervezaEnum.ASIGNADO, encontrarOrden.getOrderStatus());
      });

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).get();
         BeerOrderLine line = encontrarOrden.getBeerOrderLines().iterator().next();
         assertEquals(line.getOrderQuantity(), line.getQuantityAllocated());
      });

      BeerOrder cervezaOrdenGuardada2 = beerOrderRepository.findById(cervezaOrden.getId()).get();
      assertNotNull(cervezaOrdenGuardada2);
      assertEquals(OrdenEstadoCervezaEnum.ASIGNADO, cervezaOrdenGuardada2.getOrderStatus());
      cervezaOrdenGuardada2.getBeerOrderLines().forEach(line -> {
         assertEquals(line.getOrderQuantity(), line.getQuantityAllocated());
      });
   }

   @Test
   void testValidacionFalla() throws JsonProcessingException {
      CervezaDTO cervezaDTO = CervezaDTO.builder().id(cervezaID).upc("12345").build();
      wireMockServer.stubFor(get(CervezaServiceImpl.CERVEZA_UPC_PATH_V1 + "12345")
                                .willReturn(okJson(objectMapper.writeValueAsString(cervezaDTO))));
      BeerOrder cervezaOrden = crearOrdenCerveza();
      cervezaOrden.setCustomerRef("fallo-validacion");
      BeerOrder cervezaOrdenGuardada = cervezaOrdenManager.nuevaOrdenCerveza(cervezaOrden);

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).get();
         assertEquals(OrdenEstadoCervezaEnum.VALIDACION_EXCEPCION, encontrarOrden.getOrderStatus());
      });
   }

   @Test
   void testNuevoParaTomar() throws JsonProcessingException {
      CervezaDTO cervezaDTO = CervezaDTO.builder().id(cervezaID).upc("12345").build();
      wireMockServer.stubFor(get(CervezaServiceImpl.CERVEZA_UPC_PATH_V1 + "12345")
                                .willReturn(okJson(objectMapper.writeValueAsString(cervezaDTO))));
      BeerOrder cervezaOrden = crearOrdenCerveza();
      BeerOrder cervezaOrdenGuardada = cervezaOrdenManager.nuevaOrdenCerveza(cervezaOrden);

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).get();
         assertEquals(OrdenEstadoCervezaEnum.ASIGNADO, encontrarOrden.getOrderStatus());
      });

      cervezaOrdenManager.cervezaOrdenTomar(cervezaOrdenGuardada.getId());
      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).get();
         assertEquals(OrdenEstadoCervezaEnum.RETIRADO, encontrarOrden.getOrderStatus());
      });

      BeerOrder cervezaOrdenRetirado = beerOrderRepository.findById(cervezaOrdenGuardada.getId()).get();
      assertEquals(OrdenEstadoCervezaEnum.RETIRADO, cervezaOrdenRetirado.getOrderStatus());
   }

   @Test
   void testUbicacionFalla() throws JsonProcessingException {
      CervezaDTO cervezaDTO = CervezaDTO.builder().id(cervezaID).upc("12345").build();
      wireMockServer.stubFor(get(CervezaServiceImpl.CERVEZA_UPC_PATH_V1 + "12345")
                                .willReturn(okJson(objectMapper.writeValueAsString(cervezaDTO))));
      BeerOrder cervezaOrden = crearOrdenCerveza();
      cervezaOrden.setCustomerRef("fallo-ubicacion");
      BeerOrder cervezaOrdenGuardada = cervezaOrdenManager.nuevaOrdenCerveza(cervezaOrden);

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).get();
         assertEquals(OrdenEstadoCervezaEnum.ASIGNACION_EXCEPCION, encontrarOrden.getOrderStatus());
      });
   }

   @Test
   void testUbicacionParcial() throws JsonProcessingException {
      CervezaDTO cervezaDTO = CervezaDTO.builder().id(cervezaID).upc("12345").build();
      wireMockServer.stubFor(get(CervezaServiceImpl.CERVEZA_UPC_PATH_V1 + "12345")
                                .willReturn(okJson(objectMapper.writeValueAsString(cervezaDTO))));
      BeerOrder cervezaOrden = crearOrdenCerveza();
      cervezaOrden.setCustomerRef("ubicacion-parcial");
      BeerOrder cervezaOrdenGuardada = cervezaOrdenManager.nuevaOrdenCerveza(cervezaOrden);

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).get();
         assertEquals(OrdenEstadoCervezaEnum.INVENTARIO_PENDIENTE, encontrarOrden.getOrderStatus());
      });
   }
}