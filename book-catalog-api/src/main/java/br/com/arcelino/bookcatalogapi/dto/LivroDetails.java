package br.com.arcelino.bookcatalogapi.dto;

import java.math.BigDecimal;

public record LivroDetails(
        String titulo,
        String autor,
        String isbn,
        Integer anoPublicacao,
        BigDecimal preco,
        String genero) {

}
