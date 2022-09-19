package io.sld.riskcomplianceloginservice.repository;

import io.sld.riskcomplianceloginservice.domain.Permissions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Permissions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionsRepository extends ReactiveCrudRepository<Permissions, Long>, PermissionsRepositoryInternal {
    @Query("SELECT * FROM permissions entity WHERE entity.usuario_id = :id")
    Flux<Permissions> findByUsuario(Long id);

    @Query("SELECT * FROM permissions entity WHERE entity.usuario_id IS NULL")
    Flux<Permissions> findAllWhereUsuarioIsNull();

    @Override
    <S extends Permissions> Mono<S> save(S entity);

    @Override
    Flux<Permissions> findAll();

    @Override
    Mono<Permissions> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PermissionsRepositoryInternal {
    <S extends Permissions> Mono<S> save(S entity);

    Flux<Permissions> findAllBy(Pageable pageable);

    Flux<Permissions> findAll();

    Mono<Permissions> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Permissions> findAllBy(Pageable pageable, Criteria criteria);

}
