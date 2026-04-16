package br.com.arcelino.bookcatalogapi.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "livros")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 255)
    String titulo;

    @Column(nullable = false, length = 255)
    String autor;

    @Column(length = 20, unique = true, nullable = false)
    String isbn;

    @Column(name = "ano_publicacao")
    Integer anoPublicacao;

    @Column(precision = 10, scale = 2)
    BigDecimal preco;

    @Column(name = "data_cadastro")
    LocalDateTime dataCadastro;

    @ManyToOne
    @JoinColumn(name = "genero_id")
    Genero genero;

}
