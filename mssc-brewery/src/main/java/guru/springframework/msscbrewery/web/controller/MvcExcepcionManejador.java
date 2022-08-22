package guru.springframework.msscbrewery.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class MvcExcepcionManejador {

   @ExceptionHandler(ConstraintViolationException.class)
   public ResponseEntity<List> ValidacionErrorManejador(ConstraintViolationException e) {
      List<String> errores = new ArrayList<>(e.getConstraintViolations().size());

      e.getConstraintViolations().forEach(constraintViolation -> {
         errores.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage());
      });
      return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(BindException.class)
   public ResponseEntity<List> BindExcepcionManejador(BindException ex) {
      return new ResponseEntity<>(ex.getAllErrors(), HttpStatus.BAD_REQUEST);
   }
}
