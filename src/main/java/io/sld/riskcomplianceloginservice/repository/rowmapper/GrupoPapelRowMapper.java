package io.sld.riskcomplianceloginservice.repository.rowmapper;

import io.r2dbc.spi.Row;
import io.sld.riskcomplianceloginservice.domain.GrupoPapel;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link GrupoPapel}, with proper type conversions.
 */
@Service
public class GrupoPapelRowMapper implements BiFunction<Row, String, GrupoPapel> {

    private final ColumnConverter converter;

    public GrupoPapelRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link GrupoPapel} stored in the database.
     */
    @Override
    public GrupoPapel apply(Row row, String prefix) {
        GrupoPapel entity = new GrupoPapel();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdnVarGrupo(converter.fromRow(row, prefix + "_idn_var_grupo", String.class));
        entity.setIdnVarPapel(converter.fromRow(row, prefix + "_idn_var_papel", String.class));
        entity.setIdnVarUsuario(converter.fromRow(row, prefix + "_idn_var_usuario", String.class));
        entity.setIdnVarEmpresa(converter.fromRow(row, prefix + "_idn_var_empresa", String.class));
        entity.setGrupoId(converter.fromRow(row, prefix + "_grupo_id", Long.class));
        entity.setPapelId(converter.fromRow(row, prefix + "_papel_id", Long.class));
        entity.setEmpresaId(converter.fromRow(row, prefix + "_empresa_id", Long.class));
        entity.setUsuarioId(converter.fromRow(row, prefix + "_usuario_id", Long.class));
        return entity;
    }
}
