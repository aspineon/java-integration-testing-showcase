package eu.execom.labs.test_integration_showcase.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<String> fieldErrors = result.getFieldErrors()
                                         .stream()
                                         .map(fieldError -> fieldError.getDefaultMessage())
                                         .collect(Collectors.toList());
        return new ResponseEntity<List<String>>(fieldErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = JpaSystemException.class)
    public ResponseEntity<String> handleDuplicateEntryException(JpaSystemException ex) {
        return new ResponseEntity<String>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return new ResponseEntity<String>(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);

    }
}
