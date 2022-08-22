package com.cerveza.cervezaservice.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class MvcExcepcionManejador {

   @ExceptionHandler(ConstraintViolationException.class)
   public ResponseEntity<List> validacionErrorManejador(ConstraintViolationException ex) {
      List<String> erroresList = new ArrayList<>(ex.getConstraintViolations().size());
      ex.getConstraintViolations().forEach(error -> erroresList.add(error.toString()));
      return new ResponseEntity<>(erroresList, HttpStatus.BAD_REQUEST);

   }
}
