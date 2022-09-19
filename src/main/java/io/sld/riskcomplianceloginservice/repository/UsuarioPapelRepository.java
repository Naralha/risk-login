package io.sld.riskcomplianceloginservice.repository;

import io.sld.riskcomplianceloginservice.domain.UsuarioPapel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the UsuarioPapel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsuarioPapelRepository extends ReactiveCrudRepository<UsuarioPapel, Long>, UsuarioPapelRepositoryInternal {
    @Query("SELECT * FROM usuario_papel entity WHERE entity.papel_id = :id")
    Flux<UsuarioPapel> findByPapel(Long id);

    @Query("SELECT * FROM usuario_papel entity WHERE entity.papel_id IS NULL")
    Flux<UsuarioPapel> findAllWherePapelIsNull();

    @Query("SELECT * FROM usuario_papel entity WHERE entity.usuario_id = :id")
    Flux<UsuarioPapel> findByUsuario(Long id);

    @Query("SELECT * FROM usuario_papel entity WHERE entity.usuario_id IS NULL")
    Flux<UsuarioPapel> findAllWhereUsuarioIsNull();

    @Override
    <S extends UsuarioPapel> Mono<S> save(S entity);

    @Override
    Flux<UsuarioPapel> findAll();

    @Override
    Mono<UsuarioPapel> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UsuarioPapelRepositoryInternal {
    <S extends UsuarioPapel> Mono<S> save(S entity);

    Flux<UsuarioPapel> findAllBy(Pageable pageable);

    Flux<UsuarioPapel> findAll();

    Mono<UsuarioPapel> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UsuarioPapel> findAllBy(Pageable pageable, Criteria criteria);

}
