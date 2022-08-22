package com.cerveza.cervezaservice.web.controller;

import com.cerveza.cervezaservice.web.model.CervezaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/cerveza")
@RestController
public class CervezaController {

   @GetMapping("/{cervezaId}")
   public ResponseEntity<CervezaDTO> getCervezaById(@PathVariable("cervezaId") UUID cervezaId) {
      return new ResponseEntity<>(CervezaDTO.builder().build(), HttpStatus.OK);
   }

   @PostMapping
   public ResponseEntity grabarNuevaCerveza(@RequestBody @Validated CervezaDTO cervezaDTO) {

      return new ResponseEntity(HttpStatus.CREATED);

   }

   @PutMapping("{cervezaId}")
   public ResponseEntity actualizarCervezaBuId(@PathVariable("cervezaId") UUID cervezaId, @RequestBody @Validated CervezaDTO cervezaDTO) {
      return new ResponseEntity(HttpStatus.NO_CONTENT);
   }

}
