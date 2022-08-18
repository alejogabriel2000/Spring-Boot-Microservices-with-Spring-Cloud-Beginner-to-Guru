package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.CervezaService;
import guru.springframework.msscbrewery.web.model.CervezaDTO;
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
@WebMvcTest(CervezaController.class)
public class CervezaControllerTest {

   @MockBean CervezaService cervezaService;

   @Autowired MockMvc mockMvc;

   @Autowired ObjectMapper objectMapper;

   CervezaDTO cervezaValida;

   @Before
   public void setUp() {
      cervezaValida = CervezaDTO.builder().id(UUID.randomUUID())
                                .nombreCerveza("Patagonia")
                                .estiloCerveza("")
                                .upc(123456789012L)
                                .build();
   }


   @Test
   public void obtenerCerveza() throws Exception {
      given(cervezaService.getCervezaById(any(UUID.class))).willReturn(cervezaValida);

      mockMvc.perform(get("/api/v1/cerveza/" + cervezaValida.getId().toString()).accept(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.id", is(cervezaValida.getId().toString())))
             .andExpect(jsonPath("$.nombreCerveza", is("Patagonia")));
   }

   @Test
   public void handlePost() throws Exception {
      CervezaDTO cervezaDTO = cervezaValida;
      cervezaDTO.setId(null);
      CervezaDTO grabarDTO = CervezaDTO.builder().id(UUID.randomUUID()).nombreCerveza("Nueva Cerveza").build();
      String cervezaDTOJson = objectMapper.writeValueAsString(cervezaDTO);

      given(cervezaService.grabarNuevaCerveza(any())).willReturn(grabarDTO);

      mockMvc.perform(post("/api/v1/cerveza/")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(cervezaDTOJson))
             .andExpect(status().isCreated());
   }


   @Test
   public void handleActualizacion() throws Exception {
      CervezaDTO cervezaDTO = cervezaValida;
      String cervezaDTOJson = objectMapper.writeValueAsString(cervezaDTO);

      mockMvc.perform(put("/api/v1/cerveza/" + cervezaValida.getId())
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(cervezaDTOJson))
             .andExpect(status().isNoContent());

      then(cervezaService).should().actualizarCerveza(any(),any());
   }

}
