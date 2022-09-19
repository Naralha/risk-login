package io.sld.riskcomplianceloginservice.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PermissionsPapelSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("idn_var_permissions", table, columnPrefix + "_idn_var_permissions"));
        columns.add(Column.aliased("idn_var_papel", table, columnPrefix + "_idn_var_papel"));
        columns.add(Column.aliased("idn_var_features", table, columnPrefix + "_idn_var_features"));
        columns.add(Column.aliased("idn_var_usuario", table, columnPrefix + "_idn_var_usuario"));

        columns.add(Column.aliased("papel_id", table, columnPrefix + "_papel_id"));
        columns.add(Column.aliased("permissions_id", table, columnPrefix + "_permissions_id"));
        columns.add(Column.aliased("features_id", table, columnPrefix + "_features_id"));
        columns.add(Column.aliased("usuario_id", table, columnPrefix + "_usuario_id"));
        return columns;
    }
}
