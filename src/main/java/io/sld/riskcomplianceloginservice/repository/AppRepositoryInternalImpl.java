package io.sld.riskcomplianceloginservice.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import io.sld.riskcomplianceloginservice.domain.App;
import io.sld.riskcomplianceloginservice.repository.rowmapper.AppRowMapper;
import io.sld.riskcomplianceloginservice.repository.rowmapper.EmpresaRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the App entity.
 */
@SuppressWarnings("unused")
class AppRepositoryInternalImpl extends SimpleR2dbcRepository<App, Long> implements AppRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EmpresaRowMapper empresaMapper;
    private final UsuarioRowMapper usuarioMapper;
    private final AppRowMapper appMapper;

    private static final Table entityTable = Table.aliased("app", EntityManager.ENTITY_ALIAS);
    private static final Table empresaTable = Table.aliased("empresa", "empresa");
    private static final Table usuarioTable = Table.aliased("usuario", "usuario");

    public AppRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EmpresaRowMapper empresaMapper,
        UsuarioRowMapper usuarioMapper,
        AppRowMapper appMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(App.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.empresaMapper = empresaMapper;
        this.usuarioMapper = usuarioMapper;
        this.appMapper = appMapper;
    }

    @Override
    public Flux<App> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<App> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AppSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EmpresaSqlHelper.getColumns(empresaTable, "empresa"));
        columns.addAll(UsuarioSqlHelper.getColumns(usuarioTable, "usuario"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(empresaTable)
            .on(Column.create("empresa_id", entityTable))
            .equals(Column.create("id", empresaTable))
            .leftOuterJoin(usuarioTable)
            .on(Column.create("usuario_id", entityTable))
            .equals(Column.create("id", usuarioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, App.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<App> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<App> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private App process(Row row, RowMetadata metadata) {
        App entity = appMapper.apply(row, "e");
        entity.setEmpresa(empresaMapper.apply(row, "empresa"));
        entity.setUsuario(usuarioMapper.apply(row, "usuario"));
        return entity;
    }

    @Override
    public <S extends App> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
