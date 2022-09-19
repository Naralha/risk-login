package io.sld.riskcomplianceloginservice.repository.rowmapper;

import io.r2dbc.spi.Row;
import io.sld.riskcomplianceloginservice.domain.UsuarioPapel;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UsuarioPapel}, with proper type conversions.
 */
@Service
public class UsuarioPapelRowMapper implements BiFunction<Row, String, UsuarioPapel> {

    private final ColumnConverter converter;

    public UsuarioPapelRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UsuarioPapel} stored in the database.
     */
    @Override
    public UsuarioPapel apply(Row row, String prefix) {
        UsuarioPapel entity = new UsuarioPapel();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdnVarUsuarioCadastrado(converter.fromRow(row, prefix + "_idn_var_usuario_cadastrado", String.class));
        entity.setIdnVarPapel(converter.fromRow(row, prefix + "_idn_var_papel", String.class));
        entity.setIdnVarUsuario(converter.fromRow(row, prefix + "_idn_var_usuario", String.class));
        entity.setPapelId(converter.fromRow(row, prefix + "_papel_id", Long.class));
        entity.setUsuarioId(converter.fromRow(row, prefix + "_usuario_id", Long.class));
        return entity;
    }
}
