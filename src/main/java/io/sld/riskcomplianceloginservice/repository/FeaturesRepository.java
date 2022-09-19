package io.sld.riskcomplianceloginservice.repository;

import io.sld.riskcomplianceloginservice.domain.Features;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Features entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeaturesRepository extends ReactiveCrudRepository<Features, Long>, FeaturesRepositoryInternal {
    @Query("SELECT * FROM features entity WHERE entity.app_id = :id")
    Flux<Features> findByApp(Long id);

    @Query("SELECT * FROM features entity WHERE entity.app_id IS NULL")
    Flux<Features> findAllWhereAppIsNull();

    @Query("SELECT * FROM features entity WHERE entity.usuario_id = :id")
    Flux<Features> findByUsuario(Long id);

    @Query("SELECT * FROM features entity WHERE entity.usuario_id IS NULL")
    Flux<Features> findAllWhereUsuarioIsNull();

    @Override
    <S extends Features> Mono<S> save(S entity);

    @Override
    Flux<Features> findAll();

    @Override
    Mono<Features> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FeaturesRepositoryInternal {
    <S extends Features> Mono<S> save(S entity);

    Flux<Features> findAllBy(Pageable pageable);

    Flux<Features> findAll();

    Mono<Features> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Features> findAllBy(Pageable pageable, Criteria criteria);

}
