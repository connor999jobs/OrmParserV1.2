package org.example.utils;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.model.TableName;
import org.example.strategy.ConnectionReadWriteSource;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ModelSqlHelper {
    private final List<String> availableFieldInDatabase;

    public String buildSQL(Object o){
        Class<? extends Object> cls = o.getClass();
        String tableName = getTableName(cls);
        String fields = getFields(cls);
        String arguments = getArguments(cls);
        return String.format("INSERT INTO %s (%s) VALUES (%s);",
                tableName, fields, arguments);
    }

    private String getArguments(Class<?> cls) {
        List<Field> fields = Arrays.asList(cls.getDeclaredFields());
        List<String> listFieldNames = fields.stream().map(Field::getName)
                .filter(availableFieldInDatabase::contains).map(field -> "?").
                collect(Collectors.toList());
        return String.join(",", listFieldNames);
    }

    private String getFields(Class<?> cls) {
        List<Field> fields = Arrays.asList(cls.getDeclaredFields());
        List<String> listFieldNames = fields.stream().map(Field::getName)
                .filter(availableFieldInDatabase::contains).
                collect(Collectors.toList());
        return String.join(",", listFieldNames);
    }

    private String getTableName(Class<?> cls) {
        return cls.getAnnotation(TableName.class).name();
    }

    @SneakyThrows
    public void bindArguments(Object o, PreparedStatement ps){
        int index = 1;
        for (Field field: o.getClass().getDeclaredFields()){
            if (availableFieldInDatabase.contains(field.getName())) {
                field.setAccessible(true);
                ps.setObject(index, field.get(o));
                index++;
            }
        }
    }

    public static List<String> getMetaInformation(ConnectionReadWriteSource source) throws SQLException {
        ResultSetMetaData metaData = source.getContent().getMetaData();
        List<String> columnNames = new ArrayList<>();
        for (int index = 1; index <= metaData.getColumnCount(); index++) {
            columnNames.add(metaData.getColumnLabel(index));
        }
        return columnNames;
    }

}
