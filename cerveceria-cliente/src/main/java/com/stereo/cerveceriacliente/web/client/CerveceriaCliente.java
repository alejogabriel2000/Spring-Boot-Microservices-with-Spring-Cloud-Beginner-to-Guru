package com.stereo.cerveceriacliente.web.client;

import com.stereo.cerveceriacliente.web.model.CervezaDTO;
import com.stereo.cerveceriacliente.web.model.ClienteDTO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

@Component
@ConfigurationProperties(value = "sfg.cerveceria", ignoreUnknownFields = false)
public class CerveceriaCliente {

   public final String CERVEZA_PATH_V1 = "/api/v1/cerveza/";
   public final String CLIENTE_PATH_V1 = "/api/v1/cliente/";

   private String apihost;

   private final RestTemplate restTemplate;

   public CerveceriaCliente(RestTemplateBuilder restTemplateBuilder) {
      this.restTemplate = restTemplateBuilder.build();
   }

   public CervezaDTO getCervezaById(UUID uuid) {
      return restTemplate.getForObject(apihost + CERVEZA_PATH_V1 + uuid.toString(), CervezaDTO.class);
   }

   public URI grabarNuevaCerveza(CervezaDTO cervezaDTO) {
      return restTemplate.postForLocation(apihost + CERVEZA_PATH_V1, cervezaDTO);
   }

   public void actualizarCerveza(UUID uuid, CervezaDTO cervezaDTO) {
      restTemplate.put(apihost + CERVEZA_PATH_V1 + uuid, cervezaDTO);
   }

   public void eliminarCerveza(UUID uuid) {
      restTemplate.delete(apihost + CERVEZA_PATH_V1 + uuid);
   }


   public ClienteDTO getClienteById(UUID uuid) {
      return restTemplate.getForObject(apihost + CLIENTE_PATH_V1 + uuid.toString(), ClienteDTO.class);
   }

   public URI grabarNuevoCliente(ClienteDTO clienteDTO) {
      return restTemplate.postForLocation(apihost + CLIENTE_PATH_V1, clienteDTO);
   }

   public void actualizarCliente(UUID uuid, ClienteDTO clienteDTO) {
      restTemplate.put(apihost + CLIENTE_PATH_V1 + uuid, clienteDTO);
   }

   public void eliminarCliente(UUID uuid) {
      restTemplate.delete(apihost + CLIENTE_PATH_V1 + uuid);
   }


   public void setApihost(String apihost) {
      this.apihost = apihost;
   }

}