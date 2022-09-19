package io.sld.riskcomplianceloginservice.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import io.sld.riskcomplianceloginservice.domain.UsuarioGrupo;
import io.sld.riskcomplianceloginservice.repository.rowmapper.GrupoRowMapper;
import io.sld.riskcomplianceloginservice.repository.rowmapper.UsuarioGrupoRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the UsuarioGrupo entity.
 */
@SuppressWarnings("unused")
class UsuarioGrupoRepositoryInternalImpl extends SimpleR2dbcRepository<UsuarioGrupo, Long> implements UsuarioGrupoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final GrupoRowMapper grupoMapper;
    private final UsuarioRowMapper usuarioMapper;
    private final UsuarioGrupoRowMapper usuariogrupoMapper;

    private static final Table entityTable = Table.aliased("usuario_grupo", EntityManager.ENTITY_ALIAS);
    private static final Table grupoTable = Table.aliased("grupo", "grupo");
    private static final Table usuarioTable = Table.aliased("usuario", "usuario");

    public UsuarioGrupoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        GrupoRowMapper grupoMapper,
        UsuarioRowMapper usuarioMapper,
        UsuarioGrupoRowMapper usuariogrupoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UsuarioGrupo.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.grupoMapper = grupoMapper;
        this.usuarioMapper = usuarioMapper;
        this.usuariogrupoMapper = usuariogrupoMapper;
    }

    @Override
    public Flux<UsuarioGrupo> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UsuarioGrupo> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UsuarioGrupoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(GrupoSqlHelper.getColumns(grupoTable, "grupo"));
        columns.addAll(UsuarioSqlHelper.getColumns(usuarioTable, "usuario"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(grupoTable)
            .on(Column.create("grupo_id", entityTable))
            .equals(Column.create("id", grupoTable))
            .leftOuterJoin(usuarioTable)
            .on(Column.create("usuario_id", entityTable))
            .equals(Column.create("id", usuarioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UsuarioGrupo.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UsuarioGrupo> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UsuarioGrupo> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private UsuarioGrupo process(Row row, RowMetadata metadata) {
        UsuarioGrupo entity = usuariogrupoMapper.apply(row, "e");
        entity.setGrupo(grupoMapper.apply(row, "grupo"));
        entity.setUsuario(usuarioMapper.apply(row, "usuario"));
        return entity;
    }

    @Override
    public <S extends UsuarioGrupo> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
