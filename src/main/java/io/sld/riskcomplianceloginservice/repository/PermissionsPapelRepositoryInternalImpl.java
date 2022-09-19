package io.sld.riskcomplianceloginservice.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import io.sld.riskcomplianceloginservice.domain.PermissionsPapel;
import io.sld.riskcomplianceloginservice.repository.rowmapper.FeaturesRowMapper;
import io.sld.riskcomplianceloginservice.repository.rowmapper.PapelRowMapper;
import io.sld.riskcomplianceloginservice.repository.rowmapper.PermissionsPapelRowMapper;
import io.sld.riskcomplianceloginservice.repository.rowmapper.PermissionsRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the PermissionsPapel entity.
 */
@SuppressWarnings("unused")
class PermissionsPapelRepositoryInternalImpl
    extends SimpleR2dbcRepository<PermissionsPapel, Long>
    implements PermissionsPapelRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PapelRowMapper papelMapper;
    private final PermissionsRowMapper permissionsMapper;
    private final FeaturesRowMapper featuresMapper;
    private final UsuarioRowMapper usuarioMapper;
    private final PermissionsPapelRowMapper permissionspapelMapper;

    private static final Table entityTable = Table.aliased("permissions_papel", EntityManager.ENTITY_ALIAS);
    private static final Table papelTable = Table.aliased("papel", "papel");
    private static final Table permissionsTable = Table.aliased("permissions", "permissions");
    private static final Table featuresTable = Table.aliased("features", "features");
    private static final Table usuarioTable = Table.aliased("usuario", "usuario");

    public PermissionsPapelRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PapelRowMapper papelMapper,
        PermissionsRowMapper permissionsMapper,
        FeaturesRowMapper featuresMapper,
        UsuarioRowMapper usuarioMapper,
        PermissionsPapelRowMapper permissionspapelMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PermissionsPapel.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.papelMapper = papelMapper;
        this.permissionsMapper = permissionsMapper;
        this.featuresMapper = featuresMapper;
        this.usuarioMapper = usuarioMapper;
        this.permissionspapelMapper = permissionspapelMapper;
    }

    @Override
    public Flux<PermissionsPapel> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PermissionsPapel> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PermissionsPapelSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PapelSqlHelper.getColumns(papelTable, "papel"));
        columns.addAll(PermissionsSqlHelper.getColumns(permissionsTable, "permissions"));
        columns.addAll(FeaturesSqlHelper.getColumns(featuresTable, "features"));
        columns.addAll(UsuarioSqlHelper.getColumns(usuarioTable, "usuario"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(papelTable)
            .on(Column.create("papel_id", entityTable))
            .equals(Column.create("id", papelTable))
            .leftOuterJoin(permissionsTable)
            .on(Column.create("permissions_id", entityTable))
            .equals(Column.create("id", permissionsTable))
            .leftOuterJoin(featuresTable)
            .on(Column.create("features_id", entityTable))
            .equals(Column.create("id", featuresTable))
            .leftOuterJoin(usuarioTable)
            .on(Column.create("usuario_id", entityTable))
            .equals(Column.create("id", usuarioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PermissionsPapel.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PermissionsPapel> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PermissionsPapel> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private PermissionsPapel process(Row row, RowMetadata metadata) {
        PermissionsPapel entity = permissionspapelMapper.apply(row, "e");
        entity.setPapel(papelMapper.apply(row, "papel"));
        entity.setPermissions(permissionsMapper.apply(row, "permissions"));
        entity.setFeatures(featuresMapper.apply(row, "features"));
        entity.setUsuario(usuarioMapper.apply(row, "usuario"));
        return entity;
    }

    @Override
    public <S extends PermissionsPapel> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
