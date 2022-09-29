package guru.springframework.msscbrewery.services.v2;

import guru.springframework.msscbrewery.web.model.v2.CervezaDTOV2;
import guru.springframework.msscbrewery.web.model.v2.CervezaEstiloEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CervezaServiceV2Impl implements CervezaServiceV2 {

   @Override
   public CervezaDTOV2 getCervezaById(UUID cervezaId) {
      return CervezaDTOV2.builder().id(UUID.randomUUID()).nombreCerveza("Patagonia").estiloCerveza(CervezaEstiloEnum.ALE).build();
   }

   @Override
   public CervezaDTOV2 grabarNuevaCerveza(CervezaDTOV2 cervezaDTO) {
      return CervezaDTOV2.builder().id(UUID.randomUUID()).build();
   }

   @Override
   public void actualizarCerveza(UUID cervezaId, CervezaDTOV2 cervezaDTO) {

   }

   @Override
   public void eliminarById(UUID cervezaID) {

   }
}
