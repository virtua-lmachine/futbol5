package titulares.entrenamiento.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
/**
 * Interceptor y manejador global de excepciones
 * Esta clase utiliza la anotación RestControllerAdvice para centralizar la captura de errores
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * atrapar errores de lógica de negocio (como nombres de usuario duplicados,
     * falta de datos históricos
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    /**
     * Intercepta los fallos producidos en el cuerpo de las peticiones
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        body.put("errors", errors);
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error de validación en los campos");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
