package br.com.arcelino.bookcatalogapi.controller;

import java.net.URI;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arcelino.bookcatalogapi.dto.LivroDetails;
import br.com.arcelino.bookcatalogapi.dto.LivroFilter;
import br.com.arcelino.bookcatalogapi.dto.LivroRequest;
import br.com.arcelino.bookcatalogapi.dto.LivroResponse;
import br.com.arcelino.bookcatalogapi.dto.ApiErrorResponse;
import br.com.arcelino.bookcatalogapi.dto.ValidationErrorResponse;
import br.com.arcelino.bookcatalogapi.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springdoc.core.annotations.ParameterObject;

@Tag(name = "Livros", description = "Endpoints para gerenciamento de livros")
@RestController
@Validated
@RequestMapping("/livros")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LivroController {

    LivroService livroService;

    @Operation(
        summary = "Listar livros com filtros e paginação", 
        description = "Retorna uma lista paginada de livros que correspondem aos filtros fornecidos. Os filtros disponíveis incluem título, autor e gênero. A resposta inclui informações básicas de cada livro. Suporta paginação e ordenação através dos parâmetros padrão do Spring Data.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso"),
            @ApiResponse(
                responseCode = "400",
                description = "Requisição inválida, por exemplo, parâmetros de filtro ou paginação inválidos",
                content = @Content(
                    schema = @Schema(implementation = ValidationErrorResponse.class),
                    examples = @ExampleObject(
                        name = "Parâmetros inválidos",
                        value = """
                            {
                              \"status\": 400,
                              \"error\": \"Requisição inválida\",
                              \"message\": \"Erro de validação nos parâmetros da requisição\",
                              \"timestamp\": \"2026-04-16T10:30:45.123Z\",
                              \"fieldErrors\": {
                            \"id\": \"O id deve ser um número maior que zero\"
                              }
                            }
                            """))
                        ),
                        @ApiResponse(
                                responseCode = "500",
                                description = "Erro interno inesperado",
                                content = @Content(
                                        schema = @Schema(implementation = ApiErrorResponse.class),
                                        examples = @ExampleObject(
                                                name = "Erro interno",
                                                value = """
                                                        {
                                                            \"status\": 500,
                                                            \"error\": \"Erro interno\",
                                                            \"message\": \"Ocorreu um erro inesperado. Tente novamente mais tarde.\",
                                                            \"timestamp\": \"2026-04-16T10:30:45.123Z\"
                                                        }
                                                        """)))
    })
    @GetMapping
    public Page<LivroResponse> getLivros(
            @Valid @ModelAttribute @ParameterObject LivroFilter filter,
            @ParameterObject Pageable pageable) {
        return livroService.getLivrosPorFiltros(filter, pageable);
    }

    @Operation(
        summary = "Obter detalhes completos de um livro", 
        description = "Retorna os detalhes completos de um livro específico, incluindo título, autor, ISBN, gênero, preço e data de publicação.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalhes do livro retornados com sucesso"),
            @ApiResponse(
                responseCode = "400",
                description = "Requisição inválida, por exemplo, id do livro não é um número positivo",
                content = @Content(
                    schema = @Schema(implementation = ValidationErrorResponse.class),
                    examples = @ExampleObject(
                        name = "Id inválido",
                        value = """
                            {
                              \"status\": 400,
                              \"error\": \"Requisição inválida\",
                              \"message\": \"Erro de validação nos parâmetros da requisição\",
                              \"timestamp\": \"2026-04-16T10:30:45.123Z\",
                              \"fieldErrors\": {
                            \"id\": \"O id deve ser um número maior que zero\"
                              }
                            }
                            """))),
            @ApiResponse(
                responseCode = "404",
                description = "Livro não encontrado para o id fornecido",
                content = @Content(
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(
                        name = "Livro não encontrado",
                        value = """
                            {
                              \"status\": 404,
                              \"error\": \"Não encontrado\",
                              \"message\": \"Livro com id 999 não encontrado\",
                              \"timestamp\": \"2026-04-16T10:30:45.123Z\"
                            }
                                                        """))),
                        @ApiResponse(
                                responseCode = "500",
                                description = "Erro interno inesperado",
                                content = @Content(
                                        schema = @Schema(implementation = ApiErrorResponse.class),
                                        examples = @ExampleObject(
                                                name = "Erro interno",
                                                value = """
                                                        {
                                                            \"status\": 500,
                                                            \"error\": \"Erro interno\",
                                                            \"message\": \"Ocorreu um erro inesperado. Tente novamente mais tarde.\",
                                                            \"timestamp\": \"2026-04-16T10:30:45.123Z\"
                                                        }
                                                        """)))
    })
    @GetMapping("{id}/details")
    public LivroDetails getLivroDetails(
            @Parameter(
                description = "ID do livro para o qual os detalhes devem ser retornados", 
                example = "1", 
                required = true) 
                @PathVariable 
                @Positive(message = "O id deve ser um número maior que zero") Long id) {
        return livroService.getLivroDetails(id);
    }

    @Operation(
        summary = "Criar um novo livro", 
        description = "Cria um novo livro com as informações fornecidas. O corpo da requisição deve conter os detalhes do livro, como título, autor, gênero, preço e data de publicação. Retorna os detalhes do livro criado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
            @ApiResponse(
                responseCode = "400",
                description = "Requisição inválida, por exemplo, dados de livro ausentes ou inválidos",
                content = @Content(
                    schema = @Schema(implementation = ValidationErrorResponse.class),
                    examples = @ExampleObject(
                        name = "Corpo inválido",
                        value = """
                            {
                              \"status\": 400,
                              \"error\": \"Requisição inválida\",
                              \"message\": \"Erro de validação nos dados enviados\",
                              \"timestamp\": \"2026-04-16T10:30:45.123Z\",
                              \"fieldErrors\": {
                            \"titulo\": \"must not be blank\",
                            \"generoId\": \"must be greater than 0\"
                              }
                            }
                                                        """))),
                        @ApiResponse(
                                responseCode = "500",
                                description = "Erro interno inesperado",
                                content = @Content(
                                        schema = @Schema(implementation = ApiErrorResponse.class),
                                        examples = @ExampleObject(
                                                name = "Erro interno",
                                                value = """
                                                        {
                                                            \"status\": 500,
                                                            \"error\": \"Erro interno\",
                                                            \"message\": \"Ocorreu um erro inesperado. Tente novamente mais tarde.\",
                                                            \"timestamp\": \"2026-04-16T10:30:45.123Z\"
                                                        }
                                                        """)))
    })
    @PostMapping
    public ResponseEntity<LivroResponse> criarLivro(@RequestBody @Valid LivroRequest request) {
        var response = livroService.criarLivro(request);
        return ResponseEntity.created(URI.create("/livros/" + response.id())).body(response);
    }

    @Operation(
        summary = "Atualizar um livro existente", 
        description = "Atualiza as informações de um livro existente com base no ID fornecido. O corpo da requisição deve conter os detalhes atualizados do livro. Retorna os detalhes do livro atualizado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(
                responseCode = "400",
                description = "Requisição inválida, por exemplo, id do livro não é um número positivo ou dados de livro inválidos",
                content = @Content(
                    schema = @Schema(implementation = ValidationErrorResponse.class),
                    examples = @ExampleObject(
                        name = "Dados inválidos",
                        value = """
                            {
                              \"status\": 400,
                              \"error\": \"Requisição inválida\",
                              \"message\": \"Erro de validação nos dados enviados\",
                              \"timestamp\": \"2026-04-16T10:30:45.123Z\",
                              \"fieldErrors\": {
                            \"isbn\": \"must not be blank\"
                              }
                            }
                            """))),
            @ApiResponse(
                responseCode = "404",
                description = "Livro não encontrado para o id fornecido",
                content = @Content(
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(
                        name = "Livro inexistente",
                        value = """
                            {
                              \"status\": 404,
                              \"error\": \"Não encontrado\",
                              \"message\": \"Livro com id 999 não encontrado\",
                              \"timestamp\": \"2026-04-16T10:30:45.123Z\"
                            }
                                                        """))),
                        @ApiResponse(
                                responseCode = "500",
                                description = "Erro interno inesperado",
                                content = @Content(
                                        schema = @Schema(implementation = ApiErrorResponse.class),
                                        examples = @ExampleObject(
                                                name = "Erro interno",
                                                value = """
                                                        {
                                                            \"status\": 500,
                                                            \"error\": \"Erro interno\",
                                                            \"message\": \"Ocorreu um erro inesperado. Tente novamente mais tarde.\",
                                                            \"timestamp\": \"2026-04-16T10:30:45.123Z\"
                                                        }
                                                        """)))
    })
    @PutMapping("{id}")
    public LivroResponse atualizarLivro(
            @Parameter(description = "ID do livro a ser atualizado", example = "1", required = true)
            @PathVariable @Positive(message = "O id deve ser um número maior que zero") Long id,
            @RequestBody @Valid LivroRequest request) {
        return livroService.atualizarLivro(id, request);
    }

    @Operation(
        summary = "Deletar um livro", 
        description = "Deleta um livro existente com base no ID fornecido. Retorna status 204 No Content se a exclusão for bem-sucedida.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso"),
            @ApiResponse(
                responseCode = "400",
                description = "Requisição inválida, por exemplo, id do livro não é um número positivo",
                content = @Content(
                    schema = @Schema(implementation = ValidationErrorResponse.class),
                    examples = @ExampleObject(
                        name = "Id inválido",
                        value = """
                            {
                              \"status\": 400,
                              \"error\": \"Requisição inválida\",
                              \"message\": \"Erro de validação nos parâmetros da requisição\",
                              \"timestamp\": \"2026-04-16T10:30:45.123Z\",
                              \"fieldErrors\": {
                            \"id\": \"O id deve ser um número maior que zero\"
                              }
                            }
                            """))),
            @ApiResponse(
                responseCode = "404",
                description = "Livro não encontrado para o id fornecido",
                content = @Content(
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(
                        name = "Livro inexistente",
                        value = """
                            {
                              \"status\": 404,
                              \"error\": \"Não encontrado\",
                              \"message\": \"Livro com id 999 não encontrado\",
                              \"timestamp\": \"2026-04-16T10:30:45.123Z\"
                            }
                                                        """))),
                        @ApiResponse(
                                responseCode = "500",
                                description = "Erro interno inesperado",
                                content = @Content(
                                        schema = @Schema(implementation = ApiErrorResponse.class),
                                        examples = @ExampleObject(
                                                name = "Erro interno",
                                                value = """
                                                        {
                                                            \"status\": 500,
                                                            \"error\": \"Erro interno\",
                                                            \"message\": \"Ocorreu um erro inesperado. Tente novamente mais tarde.\",
                                                            \"timestamp\": \"2026-04-16T10:30:45.123Z\"
                                                        }
                                                        """)))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarLivro(
            @Parameter(description = "ID do livro a ser deletado", example = "1", required = true)
            @PathVariable @Positive(message = "O id deve ser um número maior que zero") Long id) {
        livroService.deletarLivro(id);
        return ResponseEntity.noContent().build();
    }

}
