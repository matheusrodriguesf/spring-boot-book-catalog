CREATE TABLE livros (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL COMMENT 'Título do livro, não pode ser nulo.',
    autor VARCHAR(255) NOT NULL COMMENT 'Autor do livro, não pode ser nulo.',
    isbn VARCHAR(20) COMMENT 'Número ISBN do livro, pode ser nulo.',
    ano_publicacao INT COMMENT 'Ano de publicação do livro, pode ser nulo.',
    preco DECIMAL(10,2) COMMENT 'Preço do livro, pode ser nulo.',
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data e hora em que o livro foi cadastrado, valor padrão é a data e hora atual.',
    
    genero_id BIGINT COMMENT 'Chave estrangeira para a tabela de gêneros',
    
    PRIMARY KEY (id),
    CONSTRAINT fk_livro_genero
        FOREIGN KEY (genero_id) REFERENCES generos(id)
) COMMENT='Tabela para armazenar os livros do catálogo.';