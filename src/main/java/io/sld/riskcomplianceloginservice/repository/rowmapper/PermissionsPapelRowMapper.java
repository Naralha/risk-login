package io.sld.riskcomplianceloginservice.repository.rowmapper;

import io.r2dbc.spi.Row;
import io.sld.riskcomplianceloginservice.domain.PermissionsPapel;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PermissionsPapel}, with proper type conversions.
 */
@Service
public class PermissionsPapelRowMapper implements BiFunction<Row, String, PermissionsPapel> {

    private final ColumnConverter converter;

    public PermissionsPapelRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PermissionsPapel} stored in the database.
     */
    @Override
    public PermissionsPapel apply(Row row, String prefix) {
        PermissionsPapel entity = new PermissionsPapel();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdnVarPermissions(converter.fromRow(row, prefix + "_idn_var_permissions", String.class));
        entity.setIdnVarPapel(converter.fromRow(row, prefix + "_idn_var_papel", String.class));
        entity.setIdnVarFeatures(converter.fromRow(row, prefix + "_idn_var_features", String.class));
        entity.setIdnVarUsuario(converter.fromRow(row, prefix + "_idn_var_usuario", String.class));
        entity.setPapelId(converter.fromRow(row, prefix + "_papel_id", Long.class));
        entity.setPermissionsId(converter.fromRow(row, prefix + "_permissions_id", Long.class));
        entity.setFeaturesId(converter.fromRow(row, prefix + "_features_id", Long.class));
        entity.setUsuarioId(converter.fromRow(row, prefix + "_usuario_id", Long.class));
        return entity;
    }
}
