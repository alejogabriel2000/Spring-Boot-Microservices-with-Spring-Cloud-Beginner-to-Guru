package guru.springframework.brewery.web.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseItem {

   @JsonProperty("id")
   private UUID id = null;

   @JsonProperty("version")
   private Integer version = null;

   @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
   @JsonProperty("creacionFecha")
   private OffsetDateTime creacionFecha = null;

   @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
   @JsonProperty("ultimaModificacionFecha")
   private OffsetDateTime ultimaModificacionFecha = null;
}
