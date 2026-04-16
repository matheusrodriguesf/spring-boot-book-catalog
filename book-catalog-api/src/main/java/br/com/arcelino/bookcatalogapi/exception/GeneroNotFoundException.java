package br.com.arcelino.bookcatalogapi.exception;

public class GeneroNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Gênero não encontrado com id: %d";

    public GeneroNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }

}
