package guru.sfg.beer.order.service.services.cerveza;

import guru.sfg.beer.order.service.web.model.CervezaDTO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
@Service
public class CervezaServiceImpl implements CervezaService {

   public final String CERVEZA_PATH_V1 = "/api/v1/beer";
   public final String CERVEZA_UPC_PATH_V1 = "/api/v1/beerUpc/";
   private final RestTemplate restTemplate;

   private String cervezaServiceHost;

   public CervezaServiceImpl(RestTemplateBuilder restTemplateBuilder) {
      this.restTemplate = restTemplateBuilder.build();
   }

   @Override
   public Optional<CervezaDTO> getCervezaById(UUID uuid) {
      return Optional.of(restTemplate.getForObject(cervezaServiceHost + CERVEZA_PATH_V1 + uuid.toString(), CervezaDTO.class));
   }

   @Override
   public Optional<CervezaDTO> getCervezaByUpc(String upc) {
      return Optional.of(restTemplate.getForObject(cervezaServiceHost + CERVEZA_UPC_PATH_V1 + upc, CervezaDTO.class));
   }

   public void setCervezaServiceHost(String cervezaServiceHost) {
      this.cervezaServiceHost = cervezaServiceHost;
   }
}
