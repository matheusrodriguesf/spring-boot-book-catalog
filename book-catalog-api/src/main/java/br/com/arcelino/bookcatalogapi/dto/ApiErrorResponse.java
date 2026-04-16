package br.com.arcelino.bookcatalogapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta padrão de erro da API")
public record ApiErrorResponse(
        @Schema(description = "Código de status HTTP", example = "404")
        int status,
        @Schema(description = "Resumo do erro", example = "Não encontrado")
        String error,
        @Schema(description = "Mensagem detalhando o erro", example = "Livro com id 999 não encontrado")
        String message,
        @Schema(description = "Data e hora do erro em formato ISO-8601", example = "2026-04-16T10:30:45.123Z")
        String timestamp) {
}