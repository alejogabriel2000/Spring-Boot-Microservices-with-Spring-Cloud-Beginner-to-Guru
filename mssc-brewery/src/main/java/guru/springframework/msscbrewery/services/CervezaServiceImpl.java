package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.CervezaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CervezaServiceImpl implements CervezaService {

   @Override public CervezaDTO getCervezaById(UUID beerId) {
      return CervezaDTO.builder().id(UUID.randomUUID())
                       .nombreCerveza("Patagonia")
                       .estiloCerveza("Patagonia Pale Ale")
                       .build();
   }

   @Override public CervezaDTO grabarNuevaCerveza(CervezaDTO cervezaDTO) {
      return CervezaDTO.builder()
                       .id(UUID.randomUUID())
                       .build();
   }

   @Override public void actualizarCerveza(UUID cervezaId, CervezaDTO cervezaDTO) {

   }

   @Override public void eliminarById(UUID cerveaID) {
      log.debug("Borrando una cerveza....");
   }
}
