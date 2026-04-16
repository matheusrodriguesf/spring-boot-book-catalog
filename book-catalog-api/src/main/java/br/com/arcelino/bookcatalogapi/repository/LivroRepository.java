package br.com.arcelino.bookcatalogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.arcelino.bookcatalogapi.entity.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {

}
