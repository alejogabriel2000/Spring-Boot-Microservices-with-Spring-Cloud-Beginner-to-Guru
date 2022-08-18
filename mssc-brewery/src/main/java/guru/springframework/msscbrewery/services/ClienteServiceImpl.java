package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.CervezaDTO;
import guru.springframework.msscbrewery.web.model.ClienteDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ClienteServiceImpl implements ClienteService {
    @Override
    public ClienteDTO getClienteById(UUID beerId) {
        return ClienteDTO.builder().id(UUID.randomUUID())
                         .nombreCliente("Bar")
                         .build();
    }

    @Override public ClienteDTO grabarNuevaCliente(ClienteDTO clienteDTO) {
        return ClienteDTO.builder().id(UUID.randomUUID()).build();
    }

    @Override public void actualizarCliente(UUID clienteId, ClienteDTO clienteDTO) {
        log.debug("Actualizando un cliente....");
    }

    @Override public void eliminarById(UUID clienteID) {
        log.debug("Borrando un cliente....");
    }
}
