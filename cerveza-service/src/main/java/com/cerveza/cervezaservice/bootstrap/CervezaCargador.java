package com.cerveza.cervezaservice.bootstrap;

import com.cerveza.cervezaservice.domain.Cerveza;
import com.cerveza.cervezaservice.repositories.CervezaRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CervezaCargador implements CommandLineRunner {

   public static final String CERVEZA_1_UPC = "0631234200036";
   public static final String CERVEZA_2_UPC = "0631234200019";
   public static final String CERVEZA_3_UPC = "0083783375213";

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
                                        .upc(CERVEZA_1_UPC)
                                        .precio(new BigDecimal("129.50"))
                                        .build());

         cervezaRepositorio.save(Cerveza.builder()
                                        .nombreCerveza("Patagonia")
                                        .estiloCerveza("Weisse")
                                        .cantidadAPreparar(200)
                                        .stockMinimo(100)
                                        .upc(CERVEZA_2_UPC)
                                        .precio(new BigDecimal("140.50"))
                                        .build());

         cervezaRepositorio.save(Cerveza.builder()
                                        .nombreCerveza("Quilmes")
                                        .estiloCerveza("PALE_ALE")
                                        .cantidadAPreparar(200)
                                        .stockMinimo(100)
                                        .upc(CERVEZA_3_UPC)
                                        .precio(new BigDecimal("140.50"))
                                        .build());
      }
      System.out.println("Cervezas cargadas: " + cervezaRepositorio.count());
   }
}
