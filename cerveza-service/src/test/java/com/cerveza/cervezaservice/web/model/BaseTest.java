package com.cerveza.cervezaservice.web.model;

import com.cerveza.cervezaservice.bootstrap.CervezaCargador;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class BaseTest {

   @Autowired
   ObjectMapper objectMapper;

   CervezaDTO getDto() {
      return CervezaDTO.builder()
                       .nombreCerveza("NombreCerveza")
                       .estiloCerverza(EstiloCervezaEnum.ALE)
                       .id(UUID.randomUUID())
                       .fechaCreacion(OffsetDateTime.now())
                       .fechaUltimaModificacion(OffsetDateTime.now())
                       .precio(new BigDecimal("12.99"))
                       .upc(CervezaCargador.CERVEZA_3_UPC)
                       .miFechaLocal(LocalDate.now())
                       .build();
   }
}