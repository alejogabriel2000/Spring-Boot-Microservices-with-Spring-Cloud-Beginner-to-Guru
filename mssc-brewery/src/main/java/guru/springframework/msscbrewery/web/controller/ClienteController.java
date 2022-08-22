package guru.springframework.msscbrewery.web.controller;

import guru.springframework.msscbrewery.services.ClienteService;
import guru.springframework.msscbrewery.web.model.CervezaDTO;
import guru.springframework.msscbrewery.web.model.ClienteDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/cliente")
@RestController
public class ClienteController {

   private final ClienteService clienteService;

   public ClienteController(ClienteService clienteService) {
      this.clienteService = clienteService;
   }

   @GetMapping({ "/{clienteId}"})
   public ResponseEntity<ClienteDTO> getCliente(@PathVariable("clienteId") UUID clienteId){

      return new ResponseEntity<>(clienteService.getClienteById(clienteId), HttpStatus.OK);
   }


   @PostMapping public ResponseEntity handlePost(@Valid @RequestBody ClienteDTO clienteDTO) {
      ClienteDTO grabarDTO = clienteService.grabarNuevaCliente(clienteDTO);
      HttpHeaders cabeceras = new HttpHeaders();
      cabeceras.add("Location", "/api/v1/cliente/" + grabarDTO.getId().toString());
      return new ResponseEntity(cabeceras, HttpStatus.CREATED);

   }

   @PutMapping({ "/{clienteId}" })
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void handleUpdate(@PathVariable("clienteId") UUID clienteId, @Valid @RequestBody ClienteDTO clienteDTO) {
      clienteService.actualizarCliente(clienteId, clienteDTO);
   }

   @DeleteMapping({"/{clienteId}"})
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void borrarcliente(@PathVariable("clienteId") UUID clienteID) {
      clienteService.eliminarById(clienteID);
   }
}