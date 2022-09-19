package io.sld.riskcomplianceloginservice.repository;

import io.sld.riskcomplianceloginservice.domain.Grupo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Grupo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GrupoRepository extends ReactiveCrudRepository<Grupo, Long>, GrupoRepositoryInternal {
    @Query("SELECT * FROM grupo entity WHERE entity.empresa_id = :id")
    Flux<Grupo> findByEmpresa(Long id);

    @Query("SELECT * FROM grupo entity WHERE entity.empresa_id IS NULL")
    Flux<Grupo> findAllWhereEmpresaIsNull();

    @Query("SELECT * FROM grupo entity WHERE entity.usuario_id = :id")
    Flux<Grupo> findByUsuario(Long id);

    @Query("SELECT * FROM grupo entity WHERE entity.usuario_id IS NULL")
    Flux<Grupo> findAllWhereUsuarioIsNull();

    @Override
    <S extends Grupo> Mono<S> save(S entity);

    @Override
    Flux<Grupo> findAll();

    @Override
    Mono<Grupo> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface GrupoRepositoryInternal {
    <S extends Grupo> Mono<S> save(S entity);

    Flux<Grupo> findAllBy(Pageable pageable);

    Flux<Grupo> findAll();

    Mono<Grupo> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Grupo> findAllBy(Pageable pageable, Criteria criteria);

}
