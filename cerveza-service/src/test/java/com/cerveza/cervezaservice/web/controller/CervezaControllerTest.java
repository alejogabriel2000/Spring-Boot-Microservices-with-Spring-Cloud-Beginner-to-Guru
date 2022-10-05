package com.cerveza.cervezaservice.web.controller;

import com.cerveza.cervezaservice.domain.Cerveza;
import com.cerveza.cervezaservice.repositories.CervezaRepositorio;
import com.cerveza.cervezaservice.web.model.CervezaDTO;
import com.cerveza.cervezaservice.web.model.EstiloCervezaEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.generate.RestDocumentationGenerationException;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "dev.springframework.cerveza",uriPort = 80)
@WebMvcTest(CervezaController.class)
@ComponentScan(basePackages = "com.cerveza.cervezaservice.web.mappers")
class CervezaControllerTest {

   @Autowired
   MockMvc mockMvc;

   @Autowired
   ObjectMapper objectMapper;

   @MockBean
   CervezaRepositorio cervezaRepositorio;

   @Test
   void getCervezaById() throws Exception {
      given(cervezaRepositorio.findById(any())).willReturn(Optional.of(Cerveza.builder().build()));
      //mockMvc.perform(get("/api/v1/cerveza/" + UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

      mockMvc.perform(get("/api/v1/cerveza/{cervezaId}", UUID.randomUUID().toString())
                         .param("estaFria", "yes")
                         .accept(MediaType.APPLICATION_JSON))
             .andExpect(MockMvcResultMatchers.status().isOk())
             .andDo(MockMvcRestDocumentation.document("v1/cerveza-obtener",
                                                      RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("cervezaId").description("UUID de cerveza deseada para obtenerla")
                             ),
                                                      RequestDocumentation.requestParameters(
                                RequestDocumentation.parameterWithName("estaFria").description("es el parametro de consulta de cerveza fria")
                             ),
                             responseFields(
                                PayloadDocumentation.fieldWithPath("id").description("Id de la cerveza").attributes(key("constraints").value("Hola")),
                                PayloadDocumentation.fieldWithPath("version").description("Numero de version"),
                                PayloadDocumentation.fieldWithPath("fechaCreacion").description("Fecha de creacion"),
                                PayloadDocumentation.fieldWithPath("fechaUltimaModificacion").description("Fecha de modificacion"),
                                PayloadDocumentation.fieldWithPath("nombreCerveza").description("Nombre de la cerveza"),
                                PayloadDocumentation.fieldWithPath("estiloCerverza").description("Estilo de la cerveza"),
                                PayloadDocumentation.fieldWithPath("upc").description("UPC de la cerveza"),
                                PayloadDocumentation.fieldWithPath("precio").description("Precios"),
                                PayloadDocumentation.fieldWithPath("cantidad").description("Cantidad de cervezas"),
                                PayloadDocumentation.fieldWithPath("stockMinimo").description("Stock minimo"),
                                PayloadDocumentation.fieldWithPath("cantidadAPreparar").description("cantidad a prepara de la cerveza")
                             )));
   }

   @Test
   void grabarNuevaCerveza() throws Exception {

      CervezaDTO cervezaDTO = getCervezaDtoValido();
      String cervezaDTOJson = objectMapper.writeValueAsString(cervezaDTO);
      ConstrainedFields fields = new ConstrainedFields(CervezaDTO.class);

      mockMvc.perform(post("/api/v1/cerveza/")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(cervezaDTOJson))
                         .andExpect(MockMvcResultMatchers.status().isCreated())
             .andDo(MockMvcRestDocumentation.document("v1/cerveza-nuevo",
                                                      PayloadDocumentation.requestFields(
                                                         PayloadDocumentation.fieldWithPath("id").ignored(),
                                fields.withPath("version").ignored(),
                                fields.withPath("fechaCreacion").ignored(),
                                fields.withPath("fechaUltimaModificacion").ignored(),
                                fields.withPath("nombreCerveza").description("Nombre de la cerveza"),
                                fields.withPath("estiloCerverza").description("Estilo de la cerveza"),
                                fields.withPath("upc").description("UPC de la cerveza"),
                                fields.withPath("precio").description("Precios"),
                                fields.withPath("cantidad").ignored(),
                                fields.withPath("stockMinimo").ignored(),
                                fields.withPath("cantidadAPreparar").ignored()
                             )));
   }

   @Test
   void actualizarCervezaById() throws Exception {
      CervezaDTO cervezaDTO = getCervezaDtoValido();
      String cervezaDTOJson = objectMapper.writeValueAsString(cervezaDTO);

      mockMvc.perform(put("/api/v1/cerveza/" + UUID.randomUUID().toString())
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(cervezaDTOJson))
             .andExpect(status().isNoContent());
   }

   CervezaDTO getCervezaDtoValido() {
      return CervezaDTO.builder()
                       .nombreCerveza("Mi cerveza")
                       .estiloCerverza(EstiloCervezaEnum.ALE)
                       .precio(new BigDecimal("2.99"))
                       .upc(1212L)
                       .build();
   }

   private static class ConstrainedFields {
      private final ConstraintDescriptions constraintDescriptions;

      ConstrainedFields(Class<?> input) {
         this.constraintDescriptions = new ConstraintDescriptions(input);
      }

      private FieldDescriptor withPath(String path) {
         return PayloadDocumentation.fieldWithPath(path).attributes(Attributes.key("constraints").value(
            StringUtils.collectionToDelimitedString(this.constraintDescriptions.descriptionsForProperty(path), ". ")));
      }
   }
}