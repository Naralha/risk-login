package io.sld.riskcomplianceloginservice.repository.rowmapper;

import io.r2dbc.spi.Row;
import io.sld.riskcomplianceloginservice.domain.Usuario;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Usuario}, with proper type conversions.
 */
@Service
public class UsuarioRowMapper implements BiFunction<Row, String, Usuario> {

    private final ColumnConverter converter;

    public UsuarioRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Usuario} stored in the database.
     */
    @Override
    public Usuario apply(Row row, String prefix) {
        Usuario entity = new Usuario();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdnVarUsuario(converter.fromRow(row, prefix + "_idn_var_usuario", String.class));
        entity.setnVarNome(converter.fromRow(row, prefix + "_n_var_nome", String.class));
        entity.setIdnVarEmpresa(converter.fromRow(row, prefix + "_idn_var_empresa", String.class));
        entity.setIdnVarUsuarioCadastro(converter.fromRow(row, prefix + "_idn_var_usuario_cadastro", String.class));
        entity.setnVarSenha(converter.fromRow(row, prefix + "_n_var_senha", String.class));
        entity.setEmpresaId(converter.fromRow(row, prefix + "_empresa_id", Long.class));
        return entity;
    }
}
