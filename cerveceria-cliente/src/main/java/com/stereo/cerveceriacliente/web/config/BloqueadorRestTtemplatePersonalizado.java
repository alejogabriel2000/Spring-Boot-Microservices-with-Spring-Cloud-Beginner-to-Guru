package com.stereo.cerveceriacliente.web.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BloqueadorRestTtemplatePersonalizado implements RestTemplateCustomizer {

   private final Integer conexionesTotalesMaxima;
   private final Integer conexionesTotalesMaximaDefecto;
   private final Integer conexionesRequestTimeout;
   private final Integer socketTimeout;

   public BloqueadorRestTtemplatePersonalizado(@Value("${sfg.conexionestotalesmaxima}") Integer conexionesTotalesMaxima,
                                               @Value("${sfg.conexionestotalesmaximadefecto}") Integer conexionesTotalesMaximaDefecto,
                                               @Value("${sfg.conexionesrequesttimeout}") Integer conexionesRequestTimeout,
                                               @Value("${sfg.sockettimeout}") Integer sockettimeout) {
      this.conexionesTotalesMaxima = conexionesTotalesMaxima;
      this.conexionesTotalesMaximaDefecto = conexionesTotalesMaximaDefecto;
      this.conexionesRequestTimeout = conexionesRequestTimeout;
      this.socketTimeout = sockettimeout;
   }

   @Override public void customize(RestTemplate restTemplate) {
      restTemplate.setRequestFactory(this.clientHttpRequestFactory());

   }

   public ClientHttpRequestFactory clientHttpRequestFactory() {
      PoolingHttpClientConnectionManager administradorConexion = new PoolingHttpClientConnectionManager();
      administradorConexion.setMaxTotal(conexionesTotalesMaxima);
      administradorConexion.setDefaultMaxPerRoute(conexionesTotalesMaximaDefecto);

      RequestConfig requestConfig = RequestConfig
         .custom()
         .setConnectionRequestTimeout(conexionesRequestTimeout)
         .setSocketTimeout(socketTimeout)
         .build();

      CloseableHttpClient httpClient = HttpClients
         .custom()
         .setConnectionManager(administradorConexion)
         .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
         .setDefaultRequestConfig(requestConfig)
         .build();

      return new HttpComponentsClientHttpRequestFactory(httpClient);


   }
}
