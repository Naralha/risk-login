package io.sld.riskcomplianceloginservice.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import io.sld.riskcomplianceloginservice.domain.Papel;
import io.sld.riskcomplianceloginservice.repository.rowmapper.AppRowMapper;
import io.sld.riskcomplianceloginservice.repository.rowmapper.PapelRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Papel entity.
 */
@SuppressWarnings("unused")
class PapelRepositoryInternalImpl extends SimpleR2dbcRepository<Papel, Long> implements PapelRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AppRowMapper appMapper;
    private final UsuarioRowMapper usuarioMapper;
    private final PapelRowMapper papelMapper;

    private static final Table entityTable = Table.aliased("papel", EntityManager.ENTITY_ALIAS);
    private static final Table appTable = Table.aliased("app", "app");
    private static final Table usuarioTable = Table.aliased("usuario", "usuario");

    public PapelRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AppRowMapper appMapper,
        UsuarioRowMapper usuarioMapper,
        PapelRowMapper papelMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Papel.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.appMapper = appMapper;
        this.usuarioMapper = usuarioMapper;
        this.papelMapper = papelMapper;
    }

    @Override
    public Flux<Papel> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Papel> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PapelSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
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
        String select = entityManager.createSelect(selectFrom, Papel.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Papel> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Papel> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Papel process(Row row, RowMetadata metadata) {
        Papel entity = papelMapper.apply(row, "e");
        entity.setApp(appMapper.apply(row, "app"));
        entity.setUsuario(usuarioMapper.apply(row, "usuario"));
        return entity;
    }

    @Override
    public <S extends Papel> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
