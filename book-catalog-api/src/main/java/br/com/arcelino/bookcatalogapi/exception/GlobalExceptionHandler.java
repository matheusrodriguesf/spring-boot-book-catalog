package br.com.arcelino.bookcatalogapi.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.arcelino.bookcatalogapi.dto.ApiErrorResponse;
import br.com.arcelino.bookcatalogapi.dto.ValidationErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String NOT_FOUND_ERROR = "Não encontrado";
    private static final String BAD_REQUEST_ERROR = "Requisição inválida";
    private static final String INTERNAL_SERVER_ERROR = "Erro interno";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Ocorreu um erro inesperado. Tente novamente mais tarde.";
    private static final String INVALID_PARAMS_MESSAGE = "Erro de validação nos parâmetros da requisição";
    private static final String INVALID_BODY_MESSAGE = "Erro de validação nos dados enviados";
    private static final String INVALID_VALUE_DEFAULT_MESSAGE = "Valor inválido";

    @ExceptionHandler({ LivroNotFoundException.class, GeneroNotFoundException.class })
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR, ex.getMessage());
    }

    @ExceptionHandler(InvalidLivroRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidLivroRequest(InvalidLivroRequestException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST_ERROR, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() != null
                                ? fieldError.getDefaultMessage()
                                : INVALID_VALUE_DEFAULT_MESSAGE,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));

        var response = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST_ERROR,
                INVALID_BODY_MESSAGE,
                Instant.now().toString(),
                fieldErrors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> fieldErrors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> extractViolationFieldName(violation.getPropertyPath().toString()),
                        violation -> violation.getMessage(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));

        var response = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST_ERROR,
                INVALID_PARAMS_MESSAGE,
                Instant.now().toString(),
                fieldErrors);

        return ResponseEntity.badRequest().body(response);
    }

        private String extractViolationFieldName(String propertyPath) {
                var lastDot = propertyPath.lastIndexOf('.');
                if (lastDot >= 0 && lastDot + 1 < propertyPath.length()) {
                        return propertyPath.substring(lastDot + 1);
                }
                return propertyPath;
        }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericError(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR_MESSAGE);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, String error, String message) {
        var response = new ApiErrorResponse(
                status.value(),
                error,
                message,
                Instant.now().toString());
        return ResponseEntity.status(status).body(response);
    }

}
