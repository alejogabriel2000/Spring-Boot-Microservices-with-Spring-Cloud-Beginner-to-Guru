package com.cerveza.cervezaservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CervezaDTO {
   private UUID id;
   private Integer version;

   private OffsetDateTime fechaCreacion;
   private OffsetDateTime fechaUltimaModificacion;

   private String nombreCerveza;
   private EstiloCervezaEnum estiloCerverza;

   private Long upc;

   private BigDecimal precio;

   private Integer cantidad;

}
