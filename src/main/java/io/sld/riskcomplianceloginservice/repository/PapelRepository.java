package io.sld.riskcomplianceloginservice.repository;

import io.sld.riskcomplianceloginservice.domain.Papel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Papel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PapelRepository extends ReactiveCrudRepository<Papel, Long>, PapelRepositoryInternal {
    @Query("SELECT * FROM papel entity WHERE entity.app_id = :id")
    Flux<Papel> findByApp(Long id);

    @Query("SELECT * FROM papel entity WHERE entity.app_id IS NULL")
    Flux<Papel> findAllWhereAppIsNull();

    @Query("SELECT * FROM papel entity WHERE entity.usuario_id = :id")
    Flux<Papel> findByUsuario(Long id);

    @Query("SELECT * FROM papel entity WHERE entity.usuario_id IS NULL")
    Flux<Papel> findAllWhereUsuarioIsNull();

    @Override
    <S extends Papel> Mono<S> save(S entity);

    @Override
    Flux<Papel> findAll();

    @Override
    Mono<Papel> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PapelRepositoryInternal {
    <S extends Papel> Mono<S> save(S entity);

    Flux<Papel> findAllBy(Pageable pageable);

    Flux<Papel> findAll();

    Mono<Papel> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Papel> findAllBy(Pageable pageable, Criteria criteria);

}
