package io.sld.riskcomplianceloginservice.repository;

import io.sld.riskcomplianceloginservice.domain.GrupoPapel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the GrupoPapel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GrupoPapelRepository extends ReactiveCrudRepository<GrupoPapel, Long>, GrupoPapelRepositoryInternal {
    @Query("SELECT * FROM grupo_papel entity WHERE entity.grupo_id = :id")
    Flux<GrupoPapel> findByGrupo(Long id);

    @Query("SELECT * FROM grupo_papel entity WHERE entity.grupo_id IS NULL")
    Flux<GrupoPapel> findAllWhereGrupoIsNull();

    @Query("SELECT * FROM grupo_papel entity WHERE entity.papel_id = :id")
    Flux<GrupoPapel> findByPapel(Long id);

    @Query("SELECT * FROM grupo_papel entity WHERE entity.papel_id IS NULL")
    Flux<GrupoPapel> findAllWherePapelIsNull();

    @Query("SELECT * FROM grupo_papel entity WHERE entity.empresa_id = :id")
    Flux<GrupoPapel> findByEmpresa(Long id);

    @Query("SELECT * FROM grupo_papel entity WHERE entity.empresa_id IS NULL")
    Flux<GrupoPapel> findAllWhereEmpresaIsNull();

    @Query("SELECT * FROM grupo_papel entity WHERE entity.usuario_id = :id")
    Flux<GrupoPapel> findByUsuario(Long id);

    @Query("SELECT * FROM grupo_papel entity WHERE entity.usuario_id IS NULL")
    Flux<GrupoPapel> findAllWhereUsuarioIsNull();

    @Override
    <S extends GrupoPapel> Mono<S> save(S entity);

    @Override
    Flux<GrupoPapel> findAll();

    @Override
    Mono<GrupoPapel> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface GrupoPapelRepositoryInternal {
    <S extends GrupoPapel> Mono<S> save(S entity);

    Flux<GrupoPapel> findAllBy(Pageable pageable);

    Flux<GrupoPapel> findAll();

    Mono<GrupoPapel> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<GrupoPapel> findAllBy(Pageable pageable, Criteria criteria);

}
