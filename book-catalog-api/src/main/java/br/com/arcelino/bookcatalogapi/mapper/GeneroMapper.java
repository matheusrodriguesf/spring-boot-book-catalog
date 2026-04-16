package br.com.arcelino.bookcatalogapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.arcelino.bookcatalogapi.dto.GeneroRequest;
import br.com.arcelino.bookcatalogapi.dto.GeneroResponse;
import br.com.arcelino.bookcatalogapi.entity.Genero;

@Mapper(componentModel = "spring")
public interface GeneroMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "livros", ignore = true)
    Genero toEntity(GeneroRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "livros", ignore = true)
    void updateEntity(GeneroRequest request, @MappingTarget Genero genero);

    GeneroResponse toResponse(Genero genero);

}
