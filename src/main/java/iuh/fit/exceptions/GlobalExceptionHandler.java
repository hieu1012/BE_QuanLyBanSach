package iuh.fit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleItemNotFound(ItemNotFoundException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbidden(ForbiddenException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(ValidationException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());
        response.put("errors", ex.getErrors());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Validation failed");

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        response.put("errors", errors);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", "Đã xảy ra lỗi: " + ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}