package guru.springframework.msscbrewery.web.controller.v2;


import guru.springframework.msscbrewery.services.v2.CervezaServiceV2;
import guru.springframework.msscbrewery.web.model.v2.CervezaDTOV2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Validated
@RequestMapping("/api/v2/cerveza") @RestController
public class CervezaControllerV2 {

   private final CervezaServiceV2 cervezaServiceV2;

   public CervezaControllerV2(CervezaServiceV2 cervezaServiceV2) {
      this.cervezaServiceV2 = cervezaServiceV2;
   }

   @GetMapping({ "/{cervezaId}" }) public ResponseEntity<CervezaDTOV2> getCerveza(@NotNull @PathVariable("cervezaId") UUID cervezaId) {

      return new ResponseEntity<>(cervezaServiceV2.getCervezaById(cervezaId), HttpStatus.OK);
   }

   @PostMapping public ResponseEntity handlePost(@Valid @NotNull  @RequestBody CervezaDTOV2 cervezaDTO) {
      CervezaDTOV2 grabarDTO = cervezaServiceV2.grabarNuevaCerveza(cervezaDTO);
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

   @PutMapping({ "/{cervezaId}" }) public ResponseEntity handleUpdate(@PathVariable("cervezaId") UUID cervezaId, @Valid @RequestBody CervezaDTOV2 cervezaDTO) {
      cervezaServiceV2.actualizarCerveza(cervezaId, cervezaDTO);
      return new ResponseEntity(HttpStatus.NO_CONTENT);
   }

   @DeleteMapping({"/{cervezaId}"})
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void borrarcerveza(UUID cervezaID) {
      cervezaServiceV2.eliminarById(cervezaID);
   }
}
