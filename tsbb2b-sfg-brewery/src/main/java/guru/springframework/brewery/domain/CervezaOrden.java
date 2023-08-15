package guru.springframework.brewery.domain;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import guru.springframework.brewery.web.model.OrdenEstadoEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class CervezaOrden extends BaseEntity {

   private String clienteRef;

   @ManyToOne
   private Cliente cliente;

   @OneToMany(mappedBy = "cervezaOrden", cascade = CascadeType.PERSIST)
   @Fetch(FetchMode.JOIN)
   private Set<LineaPedidoCerveza> lineaPedidoCerveza;

   private OrdenEstadoEnum ordenEstado = OrdenEstadoEnum.NUEVO;
   private String ordenEstadoCallbackUrl;

   @Builder
   public CervezaOrden(UUID id, Long version, Timestamp creacionFecha, Timestamp ultimaModificacionFecha, String clienteRef, Cliente cliente,
                       Set<LineaPedidoCerveza> lineaPedidoCerveza, OrdenEstadoEnum ordenEstado,
                       String ordenEstadoCallbackUrl) {
      super(id, version, creacionFecha, ultimaModificacionFecha);
      this.clienteRef = clienteRef;
      this.cliente = cliente;
      this.lineaPedidoCerveza = lineaPedidoCerveza;
      this.ordenEstado = ordenEstado;
      this.ordenEstadoCallbackUrl = ordenEstadoCallbackUrl;
   }


}