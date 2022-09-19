package io.sld.riskcomplianceloginservice.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UsuarioSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("idn_var_usuario", table, columnPrefix + "_idn_var_usuario"));
        columns.add(Column.aliased("n_var_nome", table, columnPrefix + "_n_var_nome"));
        columns.add(Column.aliased("idn_var_empresa", table, columnPrefix + "_idn_var_empresa"));
        columns.add(Column.aliased("idn_var_usuario_cadastro", table, columnPrefix + "_idn_var_usuario_cadastro"));
        columns.add(Column.aliased("n_var_senha", table, columnPrefix + "_n_var_senha"));

        columns.add(Column.aliased("empresa_id", table, columnPrefix + "_empresa_id"));
        return columns;
    }
}
