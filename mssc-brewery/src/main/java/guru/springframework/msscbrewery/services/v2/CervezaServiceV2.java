package guru.springframework.msscbrewery.services.v2;

import guru.springframework.msscbrewery.web.model.v2.CervezaDTOV2;

import java.util.UUID;

public interface CervezaServiceV2 {
   CervezaDTOV2 getCervezaById(UUID cervezaId);

   CervezaDTOV2 grabarNuevaCerveza(CervezaDTOV2 cervezaDTO);

   void actualizarCerveza(UUID cervezaId, CervezaDTOV2 cervezaDTO);

   void eliminarById(UUID cervezaID);
}
