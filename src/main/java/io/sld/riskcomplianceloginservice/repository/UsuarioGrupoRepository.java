package io.sld.riskcomplianceloginservice.repository;

import io.sld.riskcomplianceloginservice.domain.UsuarioGrupo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the UsuarioGrupo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsuarioGrupoRepository extends ReactiveCrudRepository<UsuarioGrupo, Long>, UsuarioGrupoRepositoryInternal {
    @Query("SELECT * FROM usuario_grupo entity WHERE entity.grupo_id = :id")
    Flux<UsuarioGrupo> findByGrupo(Long id);

    @Query("SELECT * FROM usuario_grupo entity WHERE entity.grupo_id IS NULL")
    Flux<UsuarioGrupo> findAllWhereGrupoIsNull();

    @Query("SELECT * FROM usuario_grupo entity WHERE entity.usuario_id = :id")
    Flux<UsuarioGrupo> findByUsuario(Long id);

    @Query("SELECT * FROM usuario_grupo entity WHERE entity.usuario_id IS NULL")
    Flux<UsuarioGrupo> findAllWhereUsuarioIsNull();

    @Override
    <S extends UsuarioGrupo> Mono<S> save(S entity);

    @Override
    Flux<UsuarioGrupo> findAll();

    @Override
    Mono<UsuarioGrupo> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UsuarioGrupoRepositoryInternal {
    <S extends UsuarioGrupo> Mono<S> save(S entity);

    Flux<UsuarioGrupo> findAllBy(Pageable pageable);

    Flux<UsuarioGrupo> findAll();

    Mono<UsuarioGrupo> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UsuarioGrupo> findAllBy(Pageable pageable, Criteria criteria);

}
