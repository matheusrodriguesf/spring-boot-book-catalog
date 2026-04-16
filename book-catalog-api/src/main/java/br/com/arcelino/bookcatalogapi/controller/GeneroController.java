package br.com.arcelino.bookcatalogapi.controller;

import java.net.URI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arcelino.bookcatalogapi.dto.GeneroRequest;
import br.com.arcelino.bookcatalogapi.dto.GeneroResponse;
import br.com.arcelino.bookcatalogapi.dto.ApiErrorResponse;
import br.com.arcelino.bookcatalogapi.dto.ValidationErrorResponse;
import br.com.arcelino.bookcatalogapi.service.GeneroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Gêneros", description = "Endpoints para gerenciamento de gêneros literários")
@RestController
@Validated
@RequestMapping("/generos")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GeneroController {

    GeneroService generoService;

    @Operation(
        summary = "Listar todos os gêneros", 
        description = "Retorna uma lista de todos os gêneros literários disponíveis no catálogo. Cada gênero inclui informações como nome e uma lista de livros associados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de gêneros retornada com sucesso"),
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
    public List<GeneroResponse> allGeneros() {
        return generoService.allGeneros();
    }

    @Operation(
        summary = "Obter detalhes de um gênero", 
        description = "Retorna os detalhes de um gênero específico com base no ID fornecido. Os detalhes incluem o nome do gênero e uma lista de livros associados a ele.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalhes do gênero retornados com sucesso"),
            @ApiResponse(
                responseCode = "400",
                description = "Requisição inválida, por exemplo, id do gênero não é um número positivo",
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
                description = "Gênero não encontrado para o id fornecido",
                content = @Content(
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(
                        name = "Gênero não encontrado",
                        value = """
                            {
                              \"status\": 404,
                              \"error\": \"Não encontrado\",
                              \"message\": \"Gênero com id 999 não encontrado\",
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
    @GetMapping("{id}")
    public GeneroResponse getGeneroById(
            @Parameter(
                description = "ID do gênero a ser recuperado",
                example = "1",
                required = true)
            @PathVariable @Positive(message = "O id deve ser um número maior que zero") Long id) {
        return generoService.getGeneroById(id);
    }

    @Operation(
        summary = "Criar um novo gênero", 
        description = "Cria um novo gênero literário com as informações fornecidas. O corpo da requisição deve conter o nome do gênero. Retorna os detalhes do gênero criado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Gênero criado com sucesso"),
            @ApiResponse(
                responseCode = "400",
                description = "Requisição inválida, por exemplo, nome do gênero ausente ou inválido",
                content = @Content(
                    schema = @Schema(implementation = ValidationErrorResponse.class),
                    examples = @ExampleObject(
                        name = "Nome inválido",
                        value = """
                            {
                              \"status\": 400,
                              \"error\": \"Requisição inválida\",
                              \"message\": \"Erro de validação nos dados enviados\",
                              \"timestamp\": \"2026-04-16T10:30:45.123Z\",
                              \"fieldErrors\": {
                            \"nome\": \"Nome do gênero é obrigatório\"
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
    public ResponseEntity<GeneroResponse> criarGenero(@RequestBody @Valid GeneroRequest request) {
        var response = generoService.criarGenero(request);
        return ResponseEntity.created(URI.create("/generos/" + response.id())).body(response);
    }

    @Operation(
        summary = "Atualizar um gênero existente", 
        description = "Atualiza as informações de um gênero existente com base no ID fornecido. O corpo da requisição deve conter o nome atualizado do gênero. Retorna os detalhes do gênero atualizado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gênero atualizado com sucesso"),
            @ApiResponse(
                responseCode = "400",
                description = "Requisição inválida, por exemplo, id do gênero não é um número positivo ou nome do gênero inválido",
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
                            \"nome\": \"Nome do gênero é obrigatório\"
                              }
                            }
                            """))),
            @ApiResponse(
                responseCode = "404",
                description = "Gênero não encontrado para o id fornecido",
                content = @Content(
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(
                        name = "Gênero inexistente",
                        value = """
                            {
                              \"status\": 404,
                              \"error\": \"Não encontrado\",
                              \"message\": \"Gênero com id 999 não encontrado\",
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
    public GeneroResponse atualizarGenero(
            @Parameter(
                description = "ID do gênero a ser atualizado",
                example = "1",
                required = true)
            @PathVariable @Positive(message = "O id deve ser um número maior que zero") Long id,
            @RequestBody @Valid GeneroRequest request) {
        return generoService.atualizarGenero(id, request);
    }

}
