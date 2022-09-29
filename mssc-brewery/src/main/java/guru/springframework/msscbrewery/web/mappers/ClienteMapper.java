package guru.springframework.msscbrewery.web.mappers;

import guru.springframework.msscbrewery.dominio.Cliente;
import guru.springframework.msscbrewery.web.model.ClienteDTO;
//import org.mapstruct.Mapper;

//@Mapper
public interface ClienteMapper {

   Cliente clienteDtoToCliente(ClienteDTO clienteDTO);

   ClienteDTO clienteToClienteDto(Cliente cliente);
}
