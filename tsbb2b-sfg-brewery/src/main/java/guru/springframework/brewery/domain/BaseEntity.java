package guru.springframework.brewery.domain;

import java.sql.Timestamp;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

   public BaseEntity(UUID id, Long version, Timestamp creacionFecha, Timestamp ultimaModificacionFecha) {
      this.id = id;
      this.version = version;
      this.creacionFecha = creacionFecha;
      this.ultimaModificacionFecha = ultimaModificacionFecha;
   }

   @Id
   @GeneratedValue(generator = "UUID")
   @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
   )
   @Type(type="org.hibernate.type.UUIDCharType")
   @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false )
   private UUID id;

   @Version
   private Long version;

   @CreationTimestamp
   @Column(updatable = false)
   private Timestamp creacionFecha;

   @UpdateTimestamp
   private Timestamp ultimaModificacionFecha;
}