package io.sld.riskcomplianceloginservice.repository.rowmapper;

import io.r2dbc.spi.Row;
import io.sld.riskcomplianceloginservice.domain.Empresa;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Empresa}, with proper type conversions.
 */
@Service
public class EmpresaRowMapper implements BiFunction<Row, String, Empresa> {

    private final ColumnConverter converter;

    public EmpresaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Empresa} stored in the database.
     */
    @Override
    public Empresa apply(Row row, String prefix) {
        Empresa entity = new Empresa();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdnVarEmpresa(converter.fromRow(row, prefix + "_idn_var_empresa", String.class));
        entity.setnVarNome(converter.fromRow(row, prefix + "_n_var_nome", String.class));
        entity.setnVarDescricao(converter.fromRow(row, prefix + "_n_var_descricao", String.class));
        return entity;
    }
}
