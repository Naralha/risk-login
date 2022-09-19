package io.sld.riskcomplianceloginservice.repository.rowmapper;

import io.r2dbc.spi.Row;
import io.sld.riskcomplianceloginservice.domain.Grupo;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Grupo}, with proper type conversions.
 */
@Service
public class GrupoRowMapper implements BiFunction<Row, String, Grupo> {

    private final ColumnConverter converter;

    public GrupoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Grupo} stored in the database.
     */
    @Override
    public Grupo apply(Row row, String prefix) {
        Grupo entity = new Grupo();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdnVarGrupo(converter.fromRow(row, prefix + "_idn_var_grupo", String.class));
        entity.setnVarNome(converter.fromRow(row, prefix + "_n_var_nome", String.class));
        entity.setIdnVarUsuario(converter.fromRow(row, prefix + "_idn_var_usuario", String.class));
        entity.setIdnVarEmpresa(converter.fromRow(row, prefix + "_idn_var_empresa", String.class));
        entity.setEmpresaId(converter.fromRow(row, prefix + "_empresa_id", Long.class));
        entity.setUsuarioId(converter.fromRow(row, prefix + "_usuario_id", Long.class));
        return entity;
    }
}
