package com.cerveza.cervezaservice.bootstrap;

import com.cerveza.cervezaservice.domain.Cerveza;
import com.cerveza.cervezaservice.repositories.CervezaRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CervezaCargador implements CommandLineRunner {

   private final CervezaRepositorio cervezaRepositorio;

   public CervezaCargador(CervezaRepositorio cervezaRepositorio) {
      this.cervezaRepositorio = cervezaRepositorio;
   }

   @Override public void run(String... args) throws Exception {
      cargarCervezaObjetos();

   }

   private void cargarCervezaObjetos() {
      if (cervezaRepositorio.count() == 0) {
         cervezaRepositorio.save(Cerveza.builder()
                                        .nombreCerveza("Patagonia")
                                        .estiloCerveza("IPA")
                                        .cantidadAPreparar(200)
                                        .stockMinimo(100)
                                        .upc(337010000001L)
                                        .precio(new BigDecimal("129.50"))
                                        .build());

         cervezaRepositorio.save(Cerveza.builder()
                                        .nombreCerveza("Patagonia")
                                        .estiloCerveza("Weisse")
                                        .cantidadAPreparar(200)
                                        .stockMinimo(100)
                                        .upc(337010000002L)
                                        .precio(new BigDecimal("140.50"))
                                        .build());
      }
      //System.out.println("Cervezas cargadas: " + cervezaRepositorio.count());
   }
}
