package io.sld.riskcomplianceloginservice.repository;

import io.sld.riskcomplianceloginservice.domain.App;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the App entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppRepository extends ReactiveCrudRepository<App, Long>, AppRepositoryInternal {
    @Query("SELECT * FROM app entity WHERE entity.empresa_id = :id")
    Flux<App> findByEmpresa(Long id);

    @Query("SELECT * FROM app entity WHERE entity.empresa_id IS NULL")
    Flux<App> findAllWhereEmpresaIsNull();

    @Query("SELECT * FROM app entity WHERE entity.usuario_id = :id")
    Flux<App> findByUsuario(Long id);

    @Query("SELECT * FROM app entity WHERE entity.usuario_id IS NULL")
    Flux<App> findAllWhereUsuarioIsNull();

    @Override
    <S extends App> Mono<S> save(S entity);

    @Override
    Flux<App> findAll();

    @Override
    Mono<App> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AppRepositoryInternal {
    <S extends App> Mono<S> save(S entity);

    Flux<App> findAllBy(Pageable pageable);

    Flux<App> findAll();

    Mono<App> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<App> findAllBy(Pageable pageable, Criteria criteria);

}
