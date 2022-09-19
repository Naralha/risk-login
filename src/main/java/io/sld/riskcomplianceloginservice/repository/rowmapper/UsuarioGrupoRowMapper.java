package io.sld.riskcomplianceloginservice.repository.rowmapper;

import io.r2dbc.spi.Row;
import io.sld.riskcomplianceloginservice.domain.UsuarioGrupo;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UsuarioGrupo}, with proper type conversions.
 */
@Service
public class UsuarioGrupoRowMapper implements BiFunction<Row, String, UsuarioGrupo> {

    private final ColumnConverter converter;

    public UsuarioGrupoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UsuarioGrupo} stored in the database.
     */
    @Override
    public UsuarioGrupo apply(Row row, String prefix) {
        UsuarioGrupo entity = new UsuarioGrupo();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdnVarUsuarioCadastrado(converter.fromRow(row, prefix + "_idn_var_usuario_cadastrado", String.class));
        entity.setIdnVarGrupo(converter.fromRow(row, prefix + "_idn_var_grupo", String.class));
        entity.setIdnVarUsuario(converter.fromRow(row, prefix + "_idn_var_usuario", String.class));
        entity.setGrupoId(converter.fromRow(row, prefix + "_grupo_id", Long.class));
        entity.setUsuarioId(converter.fromRow(row, prefix + "_usuario_id", Long.class));
        return entity;
    }
}
