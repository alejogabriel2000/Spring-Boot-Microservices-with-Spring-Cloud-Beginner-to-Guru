package com.cerveza.cervezaservice.services;

import com.cerveza.cervezaservice.web.model.CervezaDTO;

import java.util.UUID;

public interface CervezaService {
   CervezaDTO getById(UUID cervezaId);

   CervezaDTO grabarNuevaCerveza(CervezaDTO cervezaDTO);

   CervezaDTO actualizarCervezaById(UUID cervezaId, CervezaDTO cervezaDTO);
}