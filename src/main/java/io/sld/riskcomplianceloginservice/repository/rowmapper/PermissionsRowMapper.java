package io.sld.riskcomplianceloginservice.repository.rowmapper;

import io.r2dbc.spi.Row;
import io.sld.riskcomplianceloginservice.domain.Permissions;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Permissions}, with proper type conversions.
 */
@Service
public class PermissionsRowMapper implements BiFunction<Row, String, Permissions> {

    private final ColumnConverter converter;

    public PermissionsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Permissions} stored in the database.
     */
    @Override
    public Permissions apply(Row row, String prefix) {
        Permissions entity = new Permissions();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdnVarPermissions(converter.fromRow(row, prefix + "_idn_var_permissions", String.class));
        entity.setnVarNome(converter.fromRow(row, prefix + "_n_var_nome", String.class));
        entity.setnVarTipoPermissao(converter.fromRow(row, prefix + "_n_var_tipo_permissao", String.class));
        entity.setIdnVarUsuario(converter.fromRow(row, prefix + "_idn_var_usuario", String.class));
        entity.setUsuarioId(converter.fromRow(row, prefix + "_usuario_id", Long.class));
        return entity;
    }
}
