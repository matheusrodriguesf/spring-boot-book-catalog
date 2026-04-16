package br.com.arcelino.bookcatalogapi.exception;

public class InvalidLivroRequestException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Dados do livro inválidos. Verifique gênero informado e campos obrigatórios.";

    public InvalidLivroRequestException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidLivroRequestException(String message) {
        super(message);
    }
}
