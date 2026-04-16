package br.com.arcelino.bookcatalogapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import br.com.arcelino.bookcatalogapi.dto.GeneroRequest;
import br.com.arcelino.bookcatalogapi.dto.GeneroResponse;
import br.com.arcelino.bookcatalogapi.exception.GlobalExceptionHandler;
import br.com.arcelino.bookcatalogapi.service.GeneroService;

@ExtendWith(MockitoExtension.class)
class GeneroControllerTest {

        private MockMvc mockMvc;

        @Mock
    private GeneroService generoService;

        @InjectMocks
        private GeneroController generoController;

        @BeforeEach
        void setUp() {
                var validator = new LocalValidatorFactoryBean();
                validator.afterPropertiesSet();

                mockMvc = MockMvcBuilders.standaloneSetup(generoController)
                                .setControllerAdvice(new GlobalExceptionHandler())
                                .setValidator(validator)
                                .build();
        }

    @Test
    void allGeneros_shouldReturn200AndList() throws Exception {
        when(generoService.allGeneros()).thenReturn(List.of(new GeneroResponse(1L, "Fantasia")));

        mockMvc.perform(get("/generos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Fantasia"));
    }

    @Test
        void getGeneroById_shouldReturn200() throws Exception {
                when(generoService.getGeneroById(1L)).thenReturn(new GeneroResponse(1L, "Fantasia"));

                mockMvc.perform(get("/generos/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("Fantasia"));
    }

    @Test
    void criarGenero_shouldReturn201AndLocation() throws Exception {
        when(generoService.criarGenero(any(GeneroRequest.class)))
                .thenReturn(new GeneroResponse(10L, "Terror"));

        mockMvc.perform(post("/generos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Terror\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/generos/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nome").value("Terror"));
    }

    @Test
    void criarGenero_withInvalidBody_shouldReturn400() throws Exception {
        mockMvc.perform(post("/generos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.nome").value("Nome do gênero é obrigatório"));
    }

    @Test
    void atualizarGenero_shouldReturn200() throws Exception {
        when(generoService.atualizarGenero(eq(5L), any(GeneroRequest.class)))
                .thenReturn(new GeneroResponse(5L, "Drama"));

        mockMvc.perform(put("/generos/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Drama\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.nome").value("Drama"));
    }
}
