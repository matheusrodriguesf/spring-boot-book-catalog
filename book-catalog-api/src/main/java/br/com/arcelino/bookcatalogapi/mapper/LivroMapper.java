package br.com.arcelino.bookcatalogapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.arcelino.bookcatalogapi.dto.LivroDetails;
import br.com.arcelino.bookcatalogapi.dto.LivroResponse;
import br.com.arcelino.bookcatalogapi.entity.Livro;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    @Mapping(target = "genero", source = "genero.nome")
    LivroDetails toDetails(Livro livro);

    @Mapping(target = "genero", source = "genero.nome")
    LivroResponse toResponse(Livro livro);

}
