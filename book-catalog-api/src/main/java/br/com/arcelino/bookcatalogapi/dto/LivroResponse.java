package br.com.arcelino.bookcatalogapi.dto;

public record LivroResponse(
        Long id,
        String titulo,
        String autor,
        String genero) {

}
