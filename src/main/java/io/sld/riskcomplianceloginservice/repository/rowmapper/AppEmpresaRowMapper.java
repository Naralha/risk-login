package io.sld.riskcomplianceloginservice.repository.rowmapper;

import io.r2dbc.spi.Row;
import io.sld.riskcomplianceloginservice.domain.AppEmpresa;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AppEmpresa}, with proper type conversions.
 */
@Service
public class AppEmpresaRowMapper implements BiFunction<Row, String, AppEmpresa> {

    private final ColumnConverter converter;

    public AppEmpresaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AppEmpresa} stored in the database.
     */
    @Override
    public AppEmpresa apply(Row row, String prefix) {
        AppEmpresa entity = new AppEmpresa();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdnVarApp(converter.fromRow(row, prefix + "_idn_var_app", String.class));
        entity.setIdnVarEmpresa(converter.fromRow(row, prefix + "_idn_var_empresa", String.class));
        entity.setIdnVarUsuario(converter.fromRow(row, prefix + "_idn_var_usuario", String.class));
        entity.setAppId(converter.fromRow(row, prefix + "_app_id", Long.class));
        entity.setEmpresaId(converter.fromRow(row, prefix + "_empresa_id", Long.class));
        entity.setUsuarioId(converter.fromRow(row, prefix + "_usuario_id", Long.class));
        return entity;
    }
}
