package com.cerveza.cervezaservice.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cerveza {

   @Id
   @GeneratedValue(generator = "UUID")
   @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
   @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
   private UUID io;

   @Version
   private Long version;

   @CreationTimestamp
   @Column(updatable = false)
   private Timestamp fechaCreacion;

   @UpdateTimestamp
   private Timestamp fechaUltimaModificacion;
   private String nombreCerveza;
   private String estiloCerveza;

   @Column(unique = true)
   private String upc;
   private BigDecimal precio;

   private Integer stockMinimo;
   private Integer cantidadAPreparar;
}
