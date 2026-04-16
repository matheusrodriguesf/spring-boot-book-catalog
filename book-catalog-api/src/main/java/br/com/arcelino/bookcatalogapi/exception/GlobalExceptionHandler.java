package br.com.arcelino.bookcatalogapi.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String NOT_FOUND_ERROR = "Não encontrado";

    @ExceptionHandler(LivroNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleLivroNotFound(LivroNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, ex.getMessage());
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, String error, String message) {
        var response = new ApiErrorResponse(
                status.value(),
                error,
                message,
                Instant.now().toString());
        return ResponseEntity.status(status).body(response);
    }

    public record ApiErrorResponse(
            int status,
            String error,
            String message,
            String timestamp) {
    }

}
