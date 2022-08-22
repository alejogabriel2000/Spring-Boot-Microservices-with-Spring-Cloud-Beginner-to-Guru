package com.cerveza.cervezaservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CervezaDTO {

   @Null
   private UUID id;

   @Null
   private Integer version;

   @Null
   private OffsetDateTime fechaCreacion;

   @Null
   private OffsetDateTime fechaUltimaModificacion;

   @NotBlank
   private String nombreCerveza;

   @NotNull
   private EstiloCervezaEnum estiloCerverza;

   @Positive
   @NotNull
   private Long upc;

   @Positive
   @NotNull
   private BigDecimal precio;

   private Integer cantidad;

}
