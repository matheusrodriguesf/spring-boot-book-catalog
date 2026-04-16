package br.com.arcelino.bookcatalogapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.arcelino.bookcatalogapi.dto.LivroDetails;
import br.com.arcelino.bookcatalogapi.dto.LivroRequest;
import br.com.arcelino.bookcatalogapi.dto.LivroResponse;
import br.com.arcelino.bookcatalogapi.entity.Livro;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "genero", ignore = true)
    Livro toEntity(LivroRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "genero", ignore = true)
    void updateEntity(LivroRequest request, @MappingTarget Livro livro);

    @Mapping(target = "genero", source = "genero.nome")
    LivroDetails toDetails(Livro livro);

    @Mapping(target = "genero", source = "genero.nome")
    LivroResponse toResponse(Livro livro);

}
