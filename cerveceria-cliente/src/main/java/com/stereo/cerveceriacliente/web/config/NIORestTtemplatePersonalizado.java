package com.stereo.cerveceriacliente.web.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//@Component
public class NIORestTtemplatePersonalizado implements RestTemplateCustomizer {

   @Override public void customize(RestTemplate restTemplate) {
      try {
         restTemplate.setRequestFactory(this.clientHttpRequestFactory());
      } catch (IOReactorException e) {
         e.printStackTrace();
      }
   }

   public ClientHttpRequestFactory clientHttpRequestFactory() throws IOReactorException {

      final DefaultConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(
         IOReactorConfig.custom().setConnectTimeout(3000).setIoThreadCount(4).setSoTimeout(3000).build());

      final PoolingNHttpClientConnectionManager administradorConexion = new PoolingNHttpClientConnectionManager(ioReactor);
      administradorConexion.setDefaultMaxPerRoute(100);
      administradorConexion.setMaxTotal(1000);

      CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients
         .custom()
         .setConnectionManager(administradorConexion)
         .build();

      return new HttpComponentsAsyncClientHttpRequestFactory(httpAsyncClient);


   }
}
