package guru.sfg.beer.order.service.services.cerveza;

import guru.sfg.brewery.model.CervezaDTO;

import java.util.Optional;
import java.util.UUID;

public interface CervezaService {

   Optional<CervezaDTO> getCervezaById(UUID uuid);

   Optional<CervezaDTO> getCervezaByUpc(String upc);
}
