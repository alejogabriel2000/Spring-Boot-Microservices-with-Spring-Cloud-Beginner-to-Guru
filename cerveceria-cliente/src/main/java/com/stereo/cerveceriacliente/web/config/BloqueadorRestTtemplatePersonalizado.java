package com.stereo.cerveceriacliente.web.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BloqueadorRestTtemplatePersonalizado implements RestTemplateCustomizer {

   @Override public void customize(RestTemplate restTemplate) {
      restTemplate.setRequestFactory(this.clientHttpRequestFactory());

   }

   public ClientHttpRequestFactory clientHttpRequestFactory() {
      PoolingHttpClientConnectionManager administradorConexion = new PoolingHttpClientConnectionManager();
      administradorConexion.setMaxTotal(100);
      administradorConexion.setDefaultMaxPerRoute(20);

      RequestConfig requestConfig = RequestConfig
         .custom()
         .setConnectionRequestTimeout(3000)
         .setSocketTimeout(3000)
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
