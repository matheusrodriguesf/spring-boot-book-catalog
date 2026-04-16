CREATE TABLE generos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL COMMENT 'Nome do gênero literário, deve ser único.',
    
    PRIMARY KEY (id),
    UNIQUE (nome)
) COMMENT='Tabela para armazenar os gêneros literários dos livros.';