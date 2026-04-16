package br.com.arcelino.bookcatalogapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import br.com.arcelino.bookcatalogapi.dto.LivroDetails;
import br.com.arcelino.bookcatalogapi.dto.LivroFilter;
import br.com.arcelino.bookcatalogapi.dto.LivroRequest;
import br.com.arcelino.bookcatalogapi.dto.LivroResponse;
import br.com.arcelino.bookcatalogapi.exception.GlobalExceptionHandler;
import br.com.arcelino.bookcatalogapi.service.LivroService;

@ExtendWith(MockitoExtension.class)
class LivroControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LivroService livroService;

    @InjectMocks
    private LivroController livroController;

    @BeforeEach
    void setUp() {
        var validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(livroController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setValidator(validator)
                .build();
    }

    @Test
    void getLivros_shouldReturn200AndPageContent() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        var responsePage = new PageImpl<>(List.of(new LivroResponse(1L, "Livro", "Autor", "Fantasia")), pageable, 1);

        when(livroService.getLivrosPorFiltros(any(LivroFilter.class), any(Pageable.class))).thenReturn(responsePage);

        mockMvc.perform(get("/livros").param("titulo", "Livro").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].titulo").value("Livro"));
    }

    @Test
    void getLivroDetails_shouldReturn200() throws Exception {
        when(livroService.getLivroDetails(1L))
                .thenReturn(new LivroDetails("Livro", "Autor", "123", 2020, BigDecimal.TEN, "Fantasia"));

        mockMvc.perform(get("/livros/1/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Livro"))
                .andExpect(jsonPath("$.genero").value("Fantasia"));
    }

    @Test
    void criarLivro_shouldReturn201AndLocation() throws Exception {
        when(livroService.criarLivro(any(LivroRequest.class)))
                .thenReturn(new LivroResponse(10L, "Livro", "Autor", "Fantasia"));

        mockMvc.perform(post("/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"titulo\":\"Livro\"," +
                        "\"autor\":\"Autor\"," +
                        "\"isbn\":\"123456\"," +
                        "\"anoPublicacao\":2024," +
                        "\"preco\":59.9," +
                        "\"generoId\":1" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/livros/10"))
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void criarLivro_withInvalidBody_shouldReturn400() throws Exception {
        mockMvc.perform(post("/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"titulo\":\"\"," +
                        "\"autor\":\"Autor\"," +
                        "\"isbn\":\"123\"" +
                        "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.titulo").exists());
    }

    @Test
    void atualizarLivro_shouldReturn200() throws Exception {
        when(livroService.atualizarLivro(eq(5L), any(LivroRequest.class)))
                .thenReturn(new LivroResponse(5L, "Atualizado", "Autor", "Drama"));

        mockMvc.perform(put("/livros/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"titulo\":\"Atualizado\"," +
                        "\"autor\":\"Autor\"," +
                        "\"isbn\":\"999\"," +
                        "\"anoPublicacao\":2025," +
                        "\"preco\":99.9," +
                        "\"generoId\":2" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.titulo").value("Atualizado"));
    }

    @Test
    void deletarLivro_shouldReturn204() throws Exception {
        doNothing().when(livroService).deletarLivro(3L);

        mockMvc.perform(delete("/livros/3"))
                .andExpect(status().isNoContent());
    }
}
