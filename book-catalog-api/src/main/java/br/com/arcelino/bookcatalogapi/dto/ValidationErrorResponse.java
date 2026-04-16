package br.com.arcelino.bookcatalogapi.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de erro de validação da API")
public record ValidationErrorResponse(
        @Schema(description = "Código de status HTTP", example = "400")
        int status,
        @Schema(description = "Resumo do erro", example = "Requisição inválida")
        String error,
        @Schema(description = "Mensagem de contexto do erro", example = "Erro de validação nos dados enviados")
        String message,
        @Schema(description = "Data e hora do erro em formato ISO-8601", example = "2026-04-16T10:30:45.123Z")
        String timestamp,
        @Schema(
                description = "Mapa com o nome do campo e sua mensagem de erro",
                example = "{\"titulo\":\"Título não pode ter mais de 255 caracteres\",\"preco\":\"must be greater than 0.0\"}")
        Map<String, String> fieldErrors) {
}