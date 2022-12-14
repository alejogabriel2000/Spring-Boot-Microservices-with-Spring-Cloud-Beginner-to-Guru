package com.cerveza.cervezaservice.web.controller;

import com.cerveza.cervezaservice.services.CervezaService;
import com.cerveza.cervezaservice.web.model.CervezaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/cerveza")
@RestController
public class CervezaController {

   private final CervezaService cervezaService;


   @GetMapping("/{cervezaId}")
   public ResponseEntity<CervezaDTO> getCervezaById(@PathVariable("cervezaId") UUID cervezaId) {
      //return new ResponseEntity<>(CervezaDTO.builder().build(), HttpStatus.OK);

      //return new ResponseEntity<>(cervezaMapper.CervezaToCervezaDTO(cervezaRepositorio.findById(cervezaId).get()), HttpStatus.OK);
      return new ResponseEntity<>(cervezaService.getById(cervezaId), HttpStatus.OK);
   }

   @PostMapping
   public ResponseEntity grabarNuevaCerveza(@RequestBody @Validated CervezaDTO cervezaDTO) {

      //cervezaRepositorio.save(cervezaMapper.CervezaDtoTOCerveza(cervezaDTO));
      //return new ResponseEntity(HttpStatus.CREATED);

      return new ResponseEntity<>(cervezaService.grabarNuevaCerveza(cervezaDTO), HttpStatus.CREATED);

   }

   @PutMapping("{cervezaId}")
   public ResponseEntity actualizarCervezaById(@PathVariable("cervezaId") UUID cervezaId, @RequestBody @Validated CervezaDTO cervezaDTO) {

/*      cervezaRepositorio.findById(cervezaId).ifPresent(cerveza -> {
         cerveza.setNombreCerveza(cervezaDTO.getNombreCerveza());
         cerveza.setEstiloCerveza(cervezaDTO.getEstiloCerverza().name());
         cerveza.setPrecio(cervezaDTO.getPrecio());
         cerveza.setUpc(cervezaDTO.getUpc());
         cervezaRepositorio.save(cerveza);
      });
      return new ResponseEntity(HttpStatus.NO_CONTENT);*/

      return new ResponseEntity<>(cervezaService.actualizarCervezaById(cervezaId, cervezaDTO), HttpStatus.NO_CONTENT);

   }

}
