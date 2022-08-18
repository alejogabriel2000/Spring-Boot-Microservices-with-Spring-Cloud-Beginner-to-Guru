package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.CervezaDTO;

import java.util.UUID;

public interface CervezaService {
    CervezaDTO getCervezaById(UUID cervezaId);

   CervezaDTO grabarNuevaCerveza(CervezaDTO cervezaDTO);

   void actualizarCerveza(UUID cervezaId, CervezaDTO cervezaDTO);

   void eliminarById(UUID cerveaID);
}
