package io.sld.riskcomplianceloginservice.repository.rowmapper;

import io.r2dbc.spi.Row;
import io.sld.riskcomplianceloginservice.domain.App;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link App}, with proper type conversions.
 */
@Service
public class AppRowMapper implements BiFunction<Row, String, App> {

    private final ColumnConverter converter;

    public AppRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link App} stored in the database.
     */
    @Override
    public App apply(Row row, String prefix) {
        App entity = new App();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdnVarApp(converter.fromRow(row, prefix + "_idn_var_app", String.class));
        entity.setnVarNome(converter.fromRow(row, prefix + "_n_var_nome", String.class));
        entity.setIdnVarUsuario(converter.fromRow(row, prefix + "_idn_var_usuario", String.class));
        entity.setIdnVarEmpresa(converter.fromRow(row, prefix + "_idn_var_empresa", String.class));
        entity.setEmpresaId(converter.fromRow(row, prefix + "_empresa_id", Long.class));
        entity.setUsuarioId(converter.fromRow(row, prefix + "_usuario_id", Long.class));
        return entity;
    }
}
