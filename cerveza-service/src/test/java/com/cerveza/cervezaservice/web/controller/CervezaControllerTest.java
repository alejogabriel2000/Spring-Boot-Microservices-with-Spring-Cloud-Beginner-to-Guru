package com.cerveza.cervezaservice.web.controller;

import com.cerveza.cervezaservice.web.model.CervezaDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CervezaController.class)
class CervezaControllerTest {

   @Autowired
   MockMvc mockMvc;

   @Autowired ObjectMapper objectMapper;

   @Test void getCervezaById() throws Exception {
      mockMvc.perform(get("/api/v1/cerveza/" + UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk());
   }

   @Test void grabarNuevaCerveza() throws Exception {

      CervezaDTO cervezaDTO = CervezaDTO.builder().build();
      String cervezaDTOJson = objectMapper.writeValueAsString(cervezaDTO);

      mockMvc.perform(post("/api/v1/cerveza/")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(cervezaDTOJson))
             .andExpect(status().isCreated());
   }

   @Test void actualizarCervezaBuId() throws Exception {

      CervezaDTO cervezaDTO = CervezaDTO.builder().build();
      String cervezaDTOJson = objectMapper.writeValueAsString(cervezaDTO);

      mockMvc.perform(put("/api/v1/cerveza/" + UUID.randomUUID().toString())
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(cervezaDTOJson))
                        .andExpect(status().isNoContent());

   }
}