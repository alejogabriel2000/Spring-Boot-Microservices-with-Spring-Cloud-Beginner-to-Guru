package com.cerveza.cervezaservice.web.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

@JsonTest
class CervezaDTOTest extends BaseTest {

   @Autowired
   ObjectMapper objectMapper;

   @Test
   void testSerializarDto() throws JsonProcessingException {
      CervezaDTO cervezaDTO = getDto();
      String jsonString = objectMapper.writeValueAsString(cervezaDTO);
      System.out.println(jsonString);
   }

   @Test
   void testDeserializacion() throws JsonProcessingException {
      String json = "{\"version\":null,\"fechaCreacion\":\"2022-10-11T09:12:57-0300\",\"fechaUltimaModificacion\":\"2022-10-11T09:12:57-0300\",\"nombreCerveza\":\"NombreCerveza\",\"estiloCerverza\":\"ALE\",\"upc\":\"0083783375213\",\"precio\":\"12.99\",\"cantidad\":null,\"stockMinimo\":null,\"cantidadAPreparar\":null,\"miFechaLocal\":\"20221011\",\"CervezaId\":\"30d07565-f0c6-45d2-9618-15f565faaaf9\"}";
      CervezaDTO dto = objectMapper.readValue(json, CervezaDTO.class);
      System.out.println(dto);
   }
}