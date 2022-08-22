package guru.springframework.msscbrewery.web.controller;

import guru.springframework.msscbrewery.services.CervezaService;
import guru.springframework.msscbrewery.web.model.CervezaDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@Deprecated
@RequestMapping("/api/v1/cerveza") @RestController public class CervezaController {

   private final CervezaService cervezaService;

   public CervezaController(CervezaService cervezaService) {
      this.cervezaService = cervezaService;
   }

   @GetMapping({ "/{cervezaId}" }) public ResponseEntity<CervezaDTO> getCerveza(@PathVariable("cervezaId") UUID cervezaId) {

      return new ResponseEntity<>(cervezaService.getCervezaById(cervezaId), HttpStatus.OK);
   }

   @PostMapping public ResponseEntity handlePost(@Valid @RequestBody CervezaDTO cervezaDTO) {
      CervezaDTO grabarDTO = cervezaService.grabarNuevaCerveza(cervezaDTO);
      HttpHeaders cabeceras = new HttpHeaders();
      cabeceras.add("Location", "/api/v1/cerveza/" + grabarDTO.getId().toString());
      return new ResponseEntity(cabeceras, HttpStatus.CREATED);

   }

   // OTRA FORMA DE HACER EL ANTERIOR - NO LO PROBE
/*    @PostMapping
    public ResponseEntity<?> saveBeer(@RequestBody CervezaDTO cervezaDTO) {
        CervezaDTO saved = cervezaService.grabarNuevaCerveza(cervezaDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{id}").build(saved.getId());

        return ResponseEntity.created(location).build();
    }*/

   @PutMapping({ "/{cervezaId}" }) public ResponseEntity handleUpdate(@PathVariable("cervezaId") UUID cervezaId, @Valid @RequestBody CervezaDTO cervezaDTO) {
      cervezaService.actualizarCerveza(cervezaId, cervezaDTO);
      return new ResponseEntity(HttpStatus.NO_CONTENT);
   }

   @DeleteMapping({"/{cervezaId}"})
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void borrarcerveza(@PathVariable("cervezaId") UUID cervezaID) {
      cervezaService.eliminarById(cervezaID);
   }

}
