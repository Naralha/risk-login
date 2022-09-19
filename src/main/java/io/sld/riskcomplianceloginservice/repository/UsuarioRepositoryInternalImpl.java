package io.sld.riskcomplianceloginservice.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import io.sld.riskcomplianceloginservice.domain.Usuario;
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
 * Spring Data SQL reactive custom repository implementation for the Usuario entity.
 */
@SuppressWarnings("unused")
class UsuarioRepositoryInternalImpl extends SimpleR2dbcRepository<Usuario, Long> implements UsuarioRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EmpresaRowMapper empresaMapper;
    private final UsuarioRowMapper usuarioMapper;

    private static final Table entityTable = Table.aliased("usuario", EntityManager.ENTITY_ALIAS);
    private static final Table empresaTable = Table.aliased("empresa", "empresa");

    public UsuarioRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EmpresaRowMapper empresaMapper,
        UsuarioRowMapper usuarioMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Usuario.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.empresaMapper = empresaMapper;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Flux<Usuario> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Usuario> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UsuarioSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EmpresaSqlHelper.getColumns(empresaTable, "empresa"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(empresaTable)
            .on(Column.create("empresa_id", entityTable))
            .equals(Column.create("id", empresaTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Usuario.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Usuario> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Usuario> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Usuario process(Row row, RowMetadata metadata) {
        Usuario entity = usuarioMapper.apply(row, "e");
        entity.setEmpresa(empresaMapper.apply(row, "empresa"));
        return entity;
    }

    @Override
    public <S extends Usuario> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
