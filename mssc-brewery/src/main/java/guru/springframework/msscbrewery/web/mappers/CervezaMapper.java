package guru.springframework.msscbrewery.web.mappers;

import guru.springframework.msscbrewery.dominio.Cerveza;
import guru.springframework.msscbrewery.web.model.CervezaDTO;
import org.mapstruct.Mapper;

@Mapper(uses = {FechaMapper.class})
public interface CervezaMapper {

   CervezaDTO cervezaToCervezaDTO(Cerveza cerveza);

   Cerveza cervezaDtoToCerveza(CervezaDTO cervezaDTO);

}
