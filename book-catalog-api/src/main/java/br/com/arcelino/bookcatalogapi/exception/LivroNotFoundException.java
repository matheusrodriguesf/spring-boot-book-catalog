package br.com.arcelino.bookcatalogapi.exception;

public class LivroNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Livro não encontrado com id: %d";

    public LivroNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }

}
