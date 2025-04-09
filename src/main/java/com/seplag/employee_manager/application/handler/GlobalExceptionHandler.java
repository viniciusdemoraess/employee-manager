package com.seplag.employee_manager.application.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.seplag.employee_manager.application.exception.InvalidTokenException;
import com.seplag.employee_manager.infrastructure.dto.ErrorResponse;

import io.swagger.v3.oas.annotations.Hidden;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound( EntityNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Entidade não encontrada", ex.getMessage(), request.getRequestURI());
    }

   @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult()
                           .getFieldErrors()
                           .stream()
                           .map(field -> field.getField() + ": " + field.getDefaultMessage())
                           .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                           .orElse("Erro de validação");
        return buildResponse(HttpStatus.BAD_REQUEST, "Erro de validação", message, request.getRequestURI());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        String rawMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
        String detailMessage = "Violação de integridade de dados.";

        Pattern pattern = Pattern.compile("Key \\((.*?)\\)=\\((.*?)\\)");
        Matcher matcher = pattern.matcher(rawMessage);

        if (matcher.find()) {
            String field = matcher.group(1);
            String value = matcher.group(2);
            detailMessage = String.format("O valor '%s' já está sendo usado no campo '%s'.", value, field);
        }

        return buildResponse(HttpStatus.CONFLICT, rawMessage, detailMessage, request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Argumento inválido", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidTokenException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Token Inválido", ex.getMessage(), request.getRequestURI());
    }


    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, String message, String path) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                path
        );
        return ResponseEntity.status(status).body(response);
    }
}
