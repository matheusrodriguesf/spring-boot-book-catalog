package br.com.arcelino.bookcatalogapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.arcelino.bookcatalogapi.dto.LivroFilter;
import br.com.arcelino.bookcatalogapi.entity.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Query("""
            select livro
            from Livro livro
            where (:#{#filter.titulo} is null or :#{#filter.titulo} = '' or lower(livro.titulo) like concat('%', lower(:#{#filter.titulo}), '%'))
             and (:#{#filter.autor} is null or :#{#filter.autor} = '' or lower(livro.autor) like concat('%', lower(:#{#filter.autor}), '%'))
             and (:#{#filter.genero} is null or :#{#filter.genero} = '' or lower(livro.genero.nome) like concat('%', lower(:#{#filter.genero}), '%'))
            order by livro.titulo, livro.autor
            """)
        Page<Livro> buscarLivrosPorFiltros(LivroFilter filter, Pageable pageable);

}
