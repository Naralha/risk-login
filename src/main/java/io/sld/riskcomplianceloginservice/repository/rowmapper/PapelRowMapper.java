package io.sld.riskcomplianceloginservice.repository.rowmapper;

import io.r2dbc.spi.Row;
import io.sld.riskcomplianceloginservice.domain.Papel;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Papel}, with proper type conversions.
 */
@Service
public class PapelRowMapper implements BiFunction<Row, String, Papel> {

    private final ColumnConverter converter;

    public PapelRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Papel} stored in the database.
     */
    @Override
    public Papel apply(Row row, String prefix) {
        Papel entity = new Papel();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdnVarPapel(converter.fromRow(row, prefix + "_idn_var_papel", String.class));
        entity.setnVarNome(converter.fromRow(row, prefix + "_n_var_nome", String.class));
        entity.setIdnVarApp(converter.fromRow(row, prefix + "_idn_var_app", String.class));
        entity.setIdnVarUsuario(converter.fromRow(row, prefix + "_idn_var_usuario", String.class));
        entity.setAppId(converter.fromRow(row, prefix + "_app_id", Long.class));
        entity.setUsuarioId(converter.fromRow(row, prefix + "_usuario_id", Long.class));
        return entity;
    }
}
