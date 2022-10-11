package com.cerveza.cervezaservice.web.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("kebab")
@JsonTest
public class CervezaDtoKebabTest extends BaseTest {

   @Test
   void testSnake() throws JsonProcessingException {
      CervezaDTO cervezaDTO = getDto();
      String jsonString = objectMapper.writeValueAsString(cervezaDTO);
      System.out.println(jsonString);
   }
}
