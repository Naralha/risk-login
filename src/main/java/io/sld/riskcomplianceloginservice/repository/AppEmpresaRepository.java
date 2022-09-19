package io.sld.riskcomplianceloginservice.repository;

import io.sld.riskcomplianceloginservice.domain.AppEmpresa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the AppEmpresa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppEmpresaRepository extends ReactiveCrudRepository<AppEmpresa, Long>, AppEmpresaRepositoryInternal {
    @Query("SELECT * FROM app_empresa entity WHERE entity.app_id = :id")
    Flux<AppEmpresa> findByApp(Long id);

    @Query("SELECT * FROM app_empresa entity WHERE entity.app_id IS NULL")
    Flux<AppEmpresa> findAllWhereAppIsNull();

    @Query("SELECT * FROM app_empresa entity WHERE entity.empresa_id = :id")
    Flux<AppEmpresa> findByEmpresa(Long id);

    @Query("SELECT * FROM app_empresa entity WHERE entity.empresa_id IS NULL")
    Flux<AppEmpresa> findAllWhereEmpresaIsNull();

    @Query("SELECT * FROM app_empresa entity WHERE entity.usuario_id = :id")
    Flux<AppEmpresa> findByUsuario(Long id);

    @Query("SELECT * FROM app_empresa entity WHERE entity.usuario_id IS NULL")
    Flux<AppEmpresa> findAllWhereUsuarioIsNull();

    @Override
    <S extends AppEmpresa> Mono<S> save(S entity);

    @Override
    Flux<AppEmpresa> findAll();

    @Override
    Mono<AppEmpresa> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AppEmpresaRepositoryInternal {
    <S extends AppEmpresa> Mono<S> save(S entity);

    Flux<AppEmpresa> findAllBy(Pageable pageable);

    Flux<AppEmpresa> findAll();

    Mono<AppEmpresa> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AppEmpresa> findAllBy(Pageable pageable, Criteria criteria);

}
