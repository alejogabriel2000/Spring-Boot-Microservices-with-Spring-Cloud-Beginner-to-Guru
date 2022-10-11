package com.cerveza.cervezaservice.services;

import com.cerveza.cervezaservice.domain.Cerveza;
import com.cerveza.cervezaservice.repositories.CervezaRepositorio;
import com.cerveza.cervezaservice.web.controller.NoEncontradoException;
import com.cerveza.cervezaservice.web.mappers.CervezaMapper;
import com.cerveza.cervezaservice.web.model.CervezaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CervezaServiceImpl implements CervezaService {

   private final CervezaMapper cervezaMapper;
   private final CervezaRepositorio cervezaRepositorio;

   @Override
   public CervezaDTO getById(UUID cervezaId) {
      return cervezaMapper.cervezaToCervezaDTO(
               cervezaRepositorio.findById(cervezaId).orElseThrow(NoEncontradoException::new)
      );
   }

   @Override
   public CervezaDTO grabarNuevaCerveza(CervezaDTO cervezaDTO) {
      return cervezaMapper.cervezaToCervezaDTO(cervezaRepositorio.save(cervezaMapper.cervezaDtoTOCerveza(cervezaDTO)));
   }

   @Override
   public CervezaDTO actualizarCervezaById(UUID cervezaId, CervezaDTO cervezaDTO) {
      Cerveza cerveza = cervezaRepositorio.findById(cervezaId).orElseThrow(NoEncontradoException::new);
      cerveza.setNombreCerveza(cervezaDTO.getNombreCerveza());
      cerveza.setEstiloCerveza(cervezaDTO.getEstiloCerverza().name());
      cerveza.setPrecio(cervezaDTO.getPrecio());
      cerveza.setUpc(cervezaDTO.getUpc());
      return cervezaMapper.cervezaToCervezaDTO(cervezaRepositorio.save(cerveza));
   }
}