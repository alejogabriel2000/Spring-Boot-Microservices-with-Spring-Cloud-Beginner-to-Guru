package com.cerveza.cervezaservice.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CervezaDTO {

   @JsonProperty("cervezaId")
   @Null
   private UUID id;

   @Null
   private Integer version;

   @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
   @Null
   private OffsetDateTime fechaCreacion;

   //@JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
   @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
   @Null
   private OffsetDateTime fechaUltimaModificacion;

   @NotBlank
   @Size(min = 3, max = 100)
   private String nombreCerveza;

   @NotNull
   private EstiloCervezaEnum estiloCerverza;

   @NotNull
   private String upc;

   @JsonFormat(shape = JsonFormat.Shape.STRING)
   @Positive
   @NotNull
   private BigDecimal precio;

   @Positive
   private Integer cantidad;

   private Integer stockMinimo;
   private Integer cantidadAPreparar;

   @JsonSerialize(using = FechaLocalSerializer.class)
   @JsonDeserialize(using = FechaLocalDeserializer.class)
   private LocalDate miFechaLocal;

}