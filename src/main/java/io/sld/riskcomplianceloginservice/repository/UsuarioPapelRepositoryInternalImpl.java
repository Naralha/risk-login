package io.sld.riskcomplianceloginservice.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import io.sld.riskcomplianceloginservice.domain.UsuarioPapel;
import io.sld.riskcomplianceloginservice.repository.rowmapper.PapelRowMapper;
import io.sld.riskcomplianceloginservice.repository.rowmapper.UsuarioPapelRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the UsuarioPapel entity.
 */
@SuppressWarnings("unused")
class UsuarioPapelRepositoryInternalImpl extends SimpleR2dbcRepository<UsuarioPapel, Long> implements UsuarioPapelRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PapelRowMapper papelMapper;
    private final UsuarioRowMapper usuarioMapper;
    private final UsuarioPapelRowMapper usuariopapelMapper;

    private static final Table entityTable = Table.aliased("usuario_papel", EntityManager.ENTITY_ALIAS);
    private static final Table papelTable = Table.aliased("papel", "papel");
    private static final Table usuarioTable = Table.aliased("usuario", "usuario");

    public UsuarioPapelRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PapelRowMapper papelMapper,
        UsuarioRowMapper usuarioMapper,
        UsuarioPapelRowMapper usuariopapelMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UsuarioPapel.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.papelMapper = papelMapper;
        this.usuarioMapper = usuarioMapper;
        this.usuariopapelMapper = usuariopapelMapper;
    }

    @Override
    public Flux<UsuarioPapel> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UsuarioPapel> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UsuarioPapelSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PapelSqlHelper.getColumns(papelTable, "papel"));
        columns.addAll(UsuarioSqlHelper.getColumns(usuarioTable, "usuario"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(papelTable)
            .on(Column.create("papel_id", entityTable))
            .equals(Column.create("id", papelTable))
            .leftOuterJoin(usuarioTable)
            .on(Column.create("usuario_id", entityTable))
            .equals(Column.create("id", usuarioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UsuarioPapel.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UsuarioPapel> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UsuarioPapel> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private UsuarioPapel process(Row row, RowMetadata metadata) {
        UsuarioPapel entity = usuariopapelMapper.apply(row, "e");
        entity.setPapel(papelMapper.apply(row, "papel"));
        entity.setUsuario(usuarioMapper.apply(row, "usuario"));
        return entity;
    }

    @Override
    public <S extends UsuarioPapel> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
