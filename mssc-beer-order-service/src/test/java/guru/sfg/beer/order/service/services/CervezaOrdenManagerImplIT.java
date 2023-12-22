package guru.sfg.beer.order.service.services;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.domain.OrdenEstadoCervezaEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import guru.sfg.beer.order.service.services.cerveza.CervezaServiceImpl;
import guru.sfg.brewery.model.CervezaDTO;
import guru.sfg.brewery.model.events.DesasignarOrdenRequest;
import guru.sfg.brewery.model.events.UbicacionFallaEvent;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.jgroups.util.Util.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(WireMockExtension.class)
@SpringBootTest
class CervezaOrdenManagerImplIT {

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

   @Autowired
   JmsTemplate jmsTemplate;

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
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         assertEquals(OrdenEstadoCervezaEnum.ASIGNADO, Objects.requireNonNull(encontrarOrden).getOrderStatus());
      });

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         BeerOrderLine line = Objects.requireNonNull(encontrarOrden).getBeerOrderLines().iterator().next();
         assertEquals(line.getOrderQuantity(), line.getQuantityAllocated());
      });

      BeerOrder cervezaOrdenGuardada2 = beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
      assertNotNull(cervezaOrdenGuardada2);
      assertEquals(OrdenEstadoCervezaEnum.ASIGNADO, Objects.requireNonNull(cervezaOrdenGuardada2).getOrderStatus());
      cervezaOrdenGuardada2.getBeerOrderLines().forEach(line -> assertEquals(line.getOrderQuantity(), line.getQuantityAllocated()));
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
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         assertEquals(OrdenEstadoCervezaEnum.VALIDACION_EXCEPCION, Objects.requireNonNull(encontrarOrden).getOrderStatus());
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
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         assertEquals(OrdenEstadoCervezaEnum.ASIGNADO, Objects.requireNonNull(encontrarOrden).getOrderStatus());
      });

      cervezaOrdenManager.cervezaOrdenTomar(cervezaOrdenGuardada.getId());
      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         assertEquals(OrdenEstadoCervezaEnum.RETIRADO, Objects.requireNonNull(encontrarOrden).getOrderStatus());
      });

      BeerOrder cervezaOrdenRetirado = beerOrderRepository.findById(cervezaOrdenGuardada.getId()).orElse(null);
      assertEquals(OrdenEstadoCervezaEnum.RETIRADO, Objects.requireNonNull(cervezaOrdenRetirado).getOrderStatus());
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
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         assertEquals(OrdenEstadoCervezaEnum.ASIGNACION_EXCEPCION, Objects.requireNonNull(encontrarOrden).getOrderStatus());
      });

      UbicacionFallaEvent ubicacionFallaEvent = (UbicacionFallaEvent) jmsTemplate.receiveAndConvert(JmsConfig.ASIGNAR_FALLA_QUEUE);

      assertNotNull(ubicacionFallaEvent);
      assertThat(Objects.requireNonNull(ubicacionFallaEvent).getOrdenId()).isEqualTo(cervezaOrdenGuardada.getId());
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
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         assertEquals(OrdenEstadoCervezaEnum.INVENTARIO_PENDIENTE, Objects.requireNonNull(encontrarOrden).getOrderStatus());
      });
   }

   @Test
   void testValidacionPendienteACancelar() throws JsonProcessingException {
      CervezaDTO cervezaDTO = CervezaDTO.builder().id(cervezaID).upc("12345").build();
      wireMockServer.stubFor(get(CervezaServiceImpl.CERVEZA_UPC_PATH_V1 + "12345")
                                .willReturn(okJson(objectMapper.writeValueAsString(cervezaDTO))));
      BeerOrder cervezaOrden = crearOrdenCerveza();
      cervezaOrden.setCustomerRef("no-se-valido");
      BeerOrder cervezaOrdenGuardada = cervezaOrdenManager.nuevaOrdenCerveza(cervezaOrden);

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         assertEquals(OrdenEstadoCervezaEnum.VALIDACION_PENDIENTE, Objects.requireNonNull(encontrarOrden).getOrderStatus());
      });

      cervezaOrdenManager.cancelarOrden(cervezaOrdenGuardada.getId());

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         assertEquals(OrdenEstadoCervezaEnum.CANCELADO, Objects.requireNonNull(encontrarOrden).getOrderStatus());
      });
   }

   @Test
   void testUbicacionPendienteACancelar() throws JsonProcessingException {
      CervezaDTO cervezaDTO = CervezaDTO.builder().id(cervezaID).upc("12345").build();
      wireMockServer.stubFor(get(CervezaServiceImpl.CERVEZA_UPC_PATH_V1 + "12345")
                                .willReturn(okJson(objectMapper.writeValueAsString(cervezaDTO))));
      BeerOrder cervezaOrden = crearOrdenCerveza();
      cervezaOrden.setCustomerRef("no-se-ubico");
      BeerOrder cervezaOrdenGuardada = cervezaOrdenManager.nuevaOrdenCerveza(cervezaOrden);

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         assertEquals(OrdenEstadoCervezaEnum.ASIGNADO_PENDIENTE, Objects.requireNonNull(encontrarOrden).getOrderStatus());
      });

      cervezaOrdenManager.cancelarOrden(cervezaOrdenGuardada.getId());

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         assertEquals(OrdenEstadoCervezaEnum.CANCELADO, Objects.requireNonNull(encontrarOrden).getOrderStatus());
      });
   }

   @Test
   void testUbicacionACancelar() throws JsonProcessingException {
      CervezaDTO cervezaDTO = CervezaDTO.builder().id(cervezaID).upc("12345").build();
      wireMockServer.stubFor(get(CervezaServiceImpl.CERVEZA_UPC_PATH_V1 + "12345")
                                .willReturn(okJson(objectMapper.writeValueAsString(cervezaDTO))));
      BeerOrder cervezaOrden = crearOrdenCerveza();

      BeerOrder cervezaOrdenGuardada = cervezaOrdenManager.nuevaOrdenCerveza(cervezaOrden);

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         assertEquals(OrdenEstadoCervezaEnum.ASIGNADO, Objects.requireNonNull(encontrarOrden).getOrderStatus());
      });

      cervezaOrdenManager.cancelarOrden(cervezaOrdenGuardada.getId());

      await().untilAsserted(() -> {
         BeerOrder encontrarOrden =  beerOrderRepository.findById(cervezaOrden.getId()).orElse(null);
         assertEquals(OrdenEstadoCervezaEnum.CANCELADO, Objects.requireNonNull(encontrarOrden).getOrderStatus());
      });

      DesasignarOrdenRequest desasignarOrdenRequest = (DesasignarOrdenRequest) jmsTemplate.receiveAndConvert(JmsConfig.DESASIGNAR_ORDEN_QUEUE);

      assertNotNull(desasignarOrdenRequest);
      assertThat(Objects.requireNonNull(desasignarOrdenRequest).getCervezaOrden().getId()).isEqualTo(cervezaOrdenGuardada.getId());
   }
}