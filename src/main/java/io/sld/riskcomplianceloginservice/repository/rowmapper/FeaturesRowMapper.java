package io.sld.riskcomplianceloginservice.repository.rowmapper;

import io.r2dbc.spi.Row;
import io.sld.riskcomplianceloginservice.domain.Features;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Features}, with proper type conversions.
 */
@Service
public class FeaturesRowMapper implements BiFunction<Row, String, Features> {

    private final ColumnConverter converter;

    public FeaturesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Features} stored in the database.
     */
    @Override
    public Features apply(Row row, String prefix) {
        Features entity = new Features();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdnVarFeatures(converter.fromRow(row, prefix + "_idn_var_features", String.class));
        entity.setnVarNome(converter.fromRow(row, prefix + "_n_var_nome", String.class));
        entity.setIdnVarApp(converter.fromRow(row, prefix + "_idn_var_app", String.class));
        entity.setIdnVarUsuario(converter.fromRow(row, prefix + "_idn_var_usuario", String.class));
        entity.setAppId(converter.fromRow(row, prefix + "_app_id", Long.class));
        entity.setUsuarioId(converter.fromRow(row, prefix + "_usuario_id", Long.class));
        return entity;
    }
}
