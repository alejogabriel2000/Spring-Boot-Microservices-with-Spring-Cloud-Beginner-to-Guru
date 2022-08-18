package com.stereo.cerveceriacliente.web.client;

import com.stereo.cerveceriacliente.web.model.CervezaDTO;
import com.stereo.cerveceriacliente.web.model.ClienteDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CerveceriaClienteTest {

   @Autowired
   CerveceriaCliente cliente;

   @Test void getCervezaById() {
      CervezaDTO dto = cliente.getCervezaById(UUID.randomUUID() );
      assertNotNull(dto);
   }

   @Test
   void testGrabarNuevaCerveza() {
      CervezaDTO cervezaDTO = CervezaDTO.builder().nombreCerveza("Nueva Cerveza").build();
      URI uri = cliente.grabarNuevaCerveza(cervezaDTO);
      assertNotNull(uri);
      System.out.println(uri.toString());
   }

   @Test
   void testActualizarCerveza() {
      CervezaDTO cervezaDTO = CervezaDTO.builder().nombreCerveza("Nueva Cerveza").build();
      cliente.actualizarCerveza(UUID.randomUUID(), cervezaDTO);
   }

   @Test
   void testEliminarCerveza() {
      cliente.eliminarCerveza(UUID.randomUUID());
   }

   @Test void getClienteById() {
      ClienteDTO dto = cliente.getClienteById(UUID.randomUUID());
      assertNotNull(dto);
   }

   @Test
   void testGrabarNuevoCliente() {
      ClienteDTO clienteDTO = ClienteDTO.builder().nombreCliente("Nuevo cliente").build();
      URI uri = cliente.grabarNuevoCliente(clienteDTO);
      assertNotNull(uri);
      System.out.println(uri.toString());
   }

   @Test
   void testActualizarCliente() {
      ClienteDTO clienteDTO = ClienteDTO.builder().nombreCliente("Nuevo Cliente").build();
      cliente.actualizarCliente(UUID.randomUUID(), clienteDTO);
   }

   @Test
   void testEliminarCliente() {
      cliente.eliminarCliente(UUID.randomUUID());
   }
}