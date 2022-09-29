package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.CervezaService;
import guru.springframework.msscbrewery.services.v2.CervezaServiceV2;
import guru.springframework.msscbrewery.web.controller.v2.CervezaControllerV2;
import guru.springframework.msscbrewery.web.model.CervezaDTO;
import guru.springframework.msscbrewery.web.model.v2.CervezaDTOV2;
import guru.springframework.msscbrewery.web.model.v2.CervezaEstiloEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CervezaControllerV2.class)
public class CervezaControllerV2Test {

   @MockBean
   CervezaServiceV2 cervezaService;

   @Autowired
   MockMvc mockMvc;

   @Autowired
   ObjectMapper objectMapper;

   CervezaDTOV2 cervezaValida;

   @Before
   public void setUp() {
      cervezaValida = CervezaDTOV2.builder()
                                  .id(UUID.randomUUID())
                                  .nombreCerveza("Patagonia")
                                  .estiloCerveza(CervezaEstiloEnum.PISLNER)
                                  .upc(123456789012L).build();
   }

   @Test
   public void obtenerCerveza() throws Exception {
      given(cervezaService.getCervezaById(any(UUID.class))).willReturn(cervezaValida);

      mockMvc.perform(get("/api/v2/cerveza/" + cervezaValida.getId().toString()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id", is(cervezaValida.getId().toString())))
             .andExpect(jsonPath("$.nombreCerveza", is("Patagonia")));
   }

   @Test
   public void handlePost() throws Exception {
      CervezaDTOV2 cervezaDTO = cervezaValida;
      //cervezaDTO.setId(UUID.randomUUID());
      CervezaDTOV2 grabarDTO = CervezaDTOV2.builder()
                                           .id(UUID.randomUUID())
                                           .nombreCerveza("Nueva Cerveza")
                                           .estiloCerveza(CervezaEstiloEnum.ALE)
                                           .build();
      String cervezaDTOJson = objectMapper.writeValueAsString(cervezaDTO);

      given(cervezaService.grabarNuevaCerveza(any())).willReturn(grabarDTO);

      mockMvc.perform(post("/api/v2/cerveza/").contentType(MediaType.APPLICATION_JSON).content(cervezaDTOJson)).andExpect(status().isCreated());
   }

   @Test
   public void handleActualizacion() throws Exception {
      CervezaDTOV2 cervezaDTO = cervezaValida;
      //cervezaDTO.setId(null);
      cervezaDTO.setEstiloCerveza(CervezaEstiloEnum.GOSE);

      String cervezaDTOJson = objectMapper.writeValueAsString(cervezaDTO);

      mockMvc.perform(put("/api/v2/cerveza/" + UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(cervezaDTOJson))
             .andExpect(status().isNoContent());

      then(cervezaService).should().actualizarCerveza(any(), any());
   }

}
