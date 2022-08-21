package org.example.strategy.read;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.model.Table;
import org.example.model.TableName;
import org.example.strategy.ConnectionReadWriteSource;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;


@RequiredArgsConstructor
public class DatabaseParsingStrategy implements ParsingStrategy<ConnectionReadWriteSource> {
    private final Class<?> tableClass;



    @SneakyThrows
    @Override
    public Table parseToTable(ConnectionReadWriteSource connection) {
        Statement statement = connection.getContent().createStatement();

        String table = tableClass.getAnnotation(TableName.class).name();
        ResultSet rs = statement.executeQuery("SELECT * FROM " + table);
        Map<Integer, Map<String, String>> result = buildTable(rs);

        return new Table(result);
    }

    @SneakyThrows
    private Map<Integer, Map<String, String>> buildTable(ResultSet rs) {
        ResultSetMetaData metadata = rs.getMetaData();

        Map<Integer, Map<String, String>> result = new LinkedHashMap<>();
        int rowId = 0;
        while (rs.next()) {
            Map<String, String> row = new LinkedHashMap<>();
            for (int index = 1; index < metadata.getColumnCount(); index++) {
                row.put(metadata.getColumnName(index), rs.getString(index));
            }
            result.put(rowId, row);
            rowId++;
        }

        return result;
    }
}

