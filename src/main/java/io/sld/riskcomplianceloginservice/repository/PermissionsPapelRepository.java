package io.sld.riskcomplianceloginservice.repository;

import io.sld.riskcomplianceloginservice.domain.PermissionsPapel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the PermissionsPapel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionsPapelRepository extends ReactiveCrudRepository<PermissionsPapel, Long>, PermissionsPapelRepositoryInternal {
    @Query("SELECT * FROM permissions_papel entity WHERE entity.papel_id = :id")
    Flux<PermissionsPapel> findByPapel(Long id);

    @Query("SELECT * FROM permissions_papel entity WHERE entity.papel_id IS NULL")
    Flux<PermissionsPapel> findAllWherePapelIsNull();

    @Query("SELECT * FROM permissions_papel entity WHERE entity.permissions_id = :id")
    Flux<PermissionsPapel> findByPermissions(Long id);

    @Query("SELECT * FROM permissions_papel entity WHERE entity.permissions_id IS NULL")
    Flux<PermissionsPapel> findAllWherePermissionsIsNull();

    @Query("SELECT * FROM permissions_papel entity WHERE entity.features_id = :id")
    Flux<PermissionsPapel> findByFeatures(Long id);

    @Query("SELECT * FROM permissions_papel entity WHERE entity.features_id IS NULL")
    Flux<PermissionsPapel> findAllWhereFeaturesIsNull();

    @Query("SELECT * FROM permissions_papel entity WHERE entity.usuario_id = :id")
    Flux<PermissionsPapel> findByUsuario(Long id);

    @Query("SELECT * FROM permissions_papel entity WHERE entity.usuario_id IS NULL")
    Flux<PermissionsPapel> findAllWhereUsuarioIsNull();

    @Override
    <S extends PermissionsPapel> Mono<S> save(S entity);

    @Override
    Flux<PermissionsPapel> findAll();

    @Override
    Mono<PermissionsPapel> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PermissionsPapelRepositoryInternal {
    <S extends PermissionsPapel> Mono<S> save(S entity);

    Flux<PermissionsPapel> findAllBy(Pageable pageable);

    Flux<PermissionsPapel> findAll();

    Mono<PermissionsPapel> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PermissionsPapel> findAllBy(Pageable pageable, Criteria criteria);

}
