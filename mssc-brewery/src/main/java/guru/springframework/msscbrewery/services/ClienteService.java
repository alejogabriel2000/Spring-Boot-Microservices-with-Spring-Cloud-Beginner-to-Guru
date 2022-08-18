package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.ClienteDTO;

import java.util.UUID;

public interface ClienteService {
    ClienteDTO getClienteById(UUID clienteId);

    ClienteDTO grabarNuevaCliente(ClienteDTO clienteDTO);

    void actualizarCliente(UUID clienteId, ClienteDTO clienteDTO);

    void eliminarById(UUID clienteID);
}
