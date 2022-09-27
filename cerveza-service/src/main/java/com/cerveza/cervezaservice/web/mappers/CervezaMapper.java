package com.cerveza.cervezaservice.web.mappers;

import com.cerveza.cervezaservice.domain.Cerveza;
import com.cerveza.cervezaservice.web.model.CervezaDTO;
import org.mapstruct.Mapper;

@Mapper(uses = {FechaMapper.class})
public interface CervezaMapper {

   CervezaDTO CervezaToCervezaDTO(Cerveza cerveza);

   Cerveza CervezaDtoTOCerveza(CervezaDTO cervezaDTO);
}
