package guru.sfg.beer.order.service.web.mappers;

import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.services.cerveza.CervezaService;
import guru.sfg.brewery.model.BeerOrderLineDto;
import guru.sfg.brewery.model.CervezaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

public abstract class CervezaOrdenLineMapperDecorator implements BeerOrderLineMapper {

   private CervezaService cervezaService;
   private BeerOrderLineMapper beerOrderLineMapper;

   @Autowired
   public void setCervezaService(CervezaService cervezaService) {
      this.cervezaService = cervezaService;
   }

   @Autowired
   @Qualifier("delegate")
   public void setBeerOrderLineMapper(BeerOrderLineMapper beerOrderLineMapper) {
      this.beerOrderLineMapper = beerOrderLineMapper;
   }

   @Override
   public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line) {
      BeerOrderLineDto ordenLineDto = beerOrderLineMapper.beerOrderLineToDto(line);
      Optional<CervezaDTO>  cervezaDTOOptional = cervezaService.getCervezaByUpc(line.getUpc());

      cervezaDTOOptional.ifPresent(cervezaDTO -> {
         ordenLineDto.setBeerName(cervezaDTO.getBeerName());
         ordenLineDto.setBeerStyle(cervezaDTO.getBeerStyle());
         //ordenLineDto.setUpc(cervezaDTO.getUpc());
         ordenLineDto.setPrice(cervezaDTO.getPrice());
         ordenLineDto.setBeerId(cervezaDTO.getId());
      });
      return ordenLineDto;
   }
}
