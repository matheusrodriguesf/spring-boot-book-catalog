package br.com.arcelino.bookcatalogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.arcelino.bookcatalogapi.entity.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Long> {

}
