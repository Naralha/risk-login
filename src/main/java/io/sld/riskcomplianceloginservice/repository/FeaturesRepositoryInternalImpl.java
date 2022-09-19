package io.sld.riskcomplianceloginservice.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import io.sld.riskcomplianceloginservice.domain.Features;
import io.sld.riskcomplianceloginservice.repository.rowmapper.AppRowMapper;
import io.sld.riskcomplianceloginservice.repository.rowmapper.FeaturesRowMapper;
import io.sld.riskcomplianceloginservice.repository.rowmapper.UsuarioRowMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Features entity.
 */
@SuppressWarnings("unused")
class FeaturesRepositoryInternalImpl extends SimpleR2dbcRepository<Features, Long> implements FeaturesRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AppRowMapper appMapper;
    private final UsuarioRowMapper usuarioMapper;
    private final FeaturesRowMapper featuresMapper;

    private static final Table entityTable = Table.aliased("features", EntityManager.ENTITY_ALIAS);
    private static final Table appTable = Table.aliased("app", "app");
    private static final Table usuarioTable = Table.aliased("usuario", "usuario");

    public FeaturesRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AppRowMapper appMapper,
        UsuarioRowMapper usuarioMapper,
        FeaturesRowMapper featuresMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Features.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.appMapper = appMapper;
        this.usuarioMapper = usuarioMapper;
        this.featuresMapper = featuresMapper;
    }

    @Override
    public Flux<Features> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Features> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FeaturesSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(AppSqlHelper.getColumns(appTable, "app"));
        columns.addAll(UsuarioSqlHelper.getColumns(usuarioTable, "usuario"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(appTable)
            .on(Column.create("app_id", entityTable))
            .equals(Column.create("id", appTable))
            .leftOuterJoin(usuarioTable)
            .on(Column.create("usuario_id", entityTable))
            .equals(Column.create("id", usuarioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Features.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Features> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Features> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Features process(Row row, RowMetadata metadata) {
        Features entity = featuresMapper.apply(row, "e");
        entity.setApp(appMapper.apply(row, "app"));
        entity.setUsuario(usuarioMapper.apply(row, "usuario"));
        return entity;
    }

    @Override
    public <S extends Features> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
