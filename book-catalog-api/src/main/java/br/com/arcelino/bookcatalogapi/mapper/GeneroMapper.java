package br.com.arcelino.bookcatalogapi.mapper;

import org.mapstruct.Mapper;

import br.com.arcelino.bookcatalogapi.dto.GeneroResponse;
import br.com.arcelino.bookcatalogapi.entity.Genero;

@Mapper(componentModel = "spring")
public interface GeneroMapper {

    GeneroResponse toResponse(Genero genero);

}
