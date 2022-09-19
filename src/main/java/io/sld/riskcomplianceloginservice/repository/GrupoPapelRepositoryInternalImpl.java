package io.sld.riskcomplianceloginservice.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import io.sld.riskcomplianceloginservice.domain.GrupoPapel;
import io.sld.riskcomplianceloginservice.repository.rowmapper.EmpresaRowMapper;
import io.sld.riskcomplianceloginservice.repository.rowmapper.GrupoPapelRowMapper;
import io.sld.riskcomplianceloginservice.repository.rowmapper.GrupoRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the GrupoPapel entity.
 */
@SuppressWarnings("unused")
class GrupoPapelRepositoryInternalImpl extends SimpleR2dbcRepository<GrupoPapel, Long> implements GrupoPapelRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final GrupoRowMapper grupoMapper;
    private final PapelRowMapper papelMapper;
    private final EmpresaRowMapper empresaMapper;
    private final UsuarioRowMapper usuarioMapper;
    private final GrupoPapelRowMapper grupopapelMapper;

    private static final Table entityTable = Table.aliased("grupo_papel", EntityManager.ENTITY_ALIAS);
    private static final Table grupoTable = Table.aliased("grupo", "grupo");
    private static final Table papelTable = Table.aliased("papel", "papel");
    private static final Table empresaTable = Table.aliased("empresa", "empresa");
    private static final Table usuarioTable = Table.aliased("usuario", "usuario");

    public GrupoPapelRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        GrupoRowMapper grupoMapper,
        PapelRowMapper papelMapper,
        EmpresaRowMapper empresaMapper,
        UsuarioRowMapper usuarioMapper,
        GrupoPapelRowMapper grupopapelMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(GrupoPapel.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.grupoMapper = grupoMapper;
        this.papelMapper = papelMapper;
        this.empresaMapper = empresaMapper;
        this.usuarioMapper = usuarioMapper;
        this.grupopapelMapper = grupopapelMapper;
    }

    @Override
    public Flux<GrupoPapel> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<GrupoPapel> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = GrupoPapelSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(GrupoSqlHelper.getColumns(grupoTable, "grupo"));
        columns.addAll(PapelSqlHelper.getColumns(papelTable, "papel"));
        columns.addAll(EmpresaSqlHelper.getColumns(empresaTable, "empresa"));
        columns.addAll(UsuarioSqlHelper.getColumns(usuarioTable, "usuario"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(grupoTable)
            .on(Column.create("grupo_id", entityTable))
            .equals(Column.create("id", grupoTable))
            .leftOuterJoin(papelTable)
            .on(Column.create("papel_id", entityTable))
            .equals(Column.create("id", papelTable))
            .leftOuterJoin(empresaTable)
            .on(Column.create("empresa_id", entityTable))
            .equals(Column.create("id", empresaTable))
            .leftOuterJoin(usuarioTable)
            .on(Column.create("usuario_id", entityTable))
            .equals(Column.create("id", usuarioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, GrupoPapel.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<GrupoPapel> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<GrupoPapel> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private GrupoPapel process(Row row, RowMetadata metadata) {
        GrupoPapel entity = grupopapelMapper.apply(row, "e");
        entity.setGrupo(grupoMapper.apply(row, "grupo"));
        entity.setPapel(papelMapper.apply(row, "papel"));
        entity.setEmpresa(empresaMapper.apply(row, "empresa"));
        entity.setUsuario(usuarioMapper.apply(row, "usuario"));
        return entity;
    }

    @Override
    public <S extends GrupoPapel> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
