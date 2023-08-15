package guru.springframework.brewery.domain;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Cliente extends BaseEntity {

   @Builder
   public Cliente(UUID id, Long version, Timestamp creacionFecha, Timestamp ultimaModificacionFecha, String clienteNombre,
                   UUID apiKey, Set<CervezaOrden> cervezaOrdenes) {
      super(id, version, creacionFecha, ultimaModificacionFecha);
      this.clienteNombre = clienteNombre;
      this.apiKey = apiKey;
      this.cervezaOrdenes = cervezaOrdenes;
   }

   private String clienteNombre;

   @Column(length = 36, columnDefinition = "varchar")
   private UUID apiKey;

   @OneToMany(mappedBy = "cliente")
   private Set<CervezaOrden> cervezaOrdenes;
}
