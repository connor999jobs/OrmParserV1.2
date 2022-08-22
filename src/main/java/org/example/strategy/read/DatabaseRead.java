package org.example.strategy.read;

import lombok.SneakyThrows;
import org.example.model.Table;
import org.example.strategy.ConnectionReadWriteSource;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseRead implements ParsingStrategy<ConnectionReadWriteSource> {


    @SneakyThrows
    @Override
    public Table parseToTable(ConnectionReadWriteSource connection) {

        ResultSet rs = connection.getContent();
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