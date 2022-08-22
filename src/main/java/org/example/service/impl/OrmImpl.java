package org.example.service.impl;

import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.example.model.Table;
import org.example.service.Orm;
import org.example.strategy.ConnectionReadWriteSource;
import org.example.strategy.DataReadWriteSource;
import org.example.strategy.FileReadWriteSource;
import org.example.strategy.read.*;
import org.example.strategy.write.*;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class OrmImpl implements Orm {

    @Override
    public <T> List<T> readAll(DataReadWriteSource<?> source, Class<T> cls) {
        Table table = convertToTable(source);
        return convertTableToListOfClasses(table, cls);
    }

    private <T> List<T> convertTableToListOfClasses(Table table, Class<T> cls) {
        List<T> result = new ArrayList<>();
        for (int index = 0; index < table.size(); index++) {
            Map<String, String> row = table.getTableRowByIndex(index);
            T instance = reflectTableRowToClass(row, cls);
            result.add(instance);
        }
        return result;
    }

    @SneakyThrows
    private <T> T reflectTableRowToClass(Map<String, String> row, Class<T> cls) {
        T instance = cls.getDeclaredConstructor().newInstance();
        for (Field each : cls.getDeclaredFields()) {
            each.setAccessible(true);
            String value = row.get(each.getName());
            if (value != null) {
                each.set(instance, transformValueToFieldType(each, value));
            }
        }
        return instance;
    }

    private Object transformValueToFieldType(Field each, String value) {
        Map<Class<?>, Function<String, Object>> typeToFunction = new LinkedHashMap<>();
        typeToFunction.put(String.class, s -> s);
        typeToFunction.put(Integer.class, Integer::parseInt);
        typeToFunction.put(Float.class, Float::parseFloat);
        typeToFunction.put(LocalDate.class, LocalDate::parse);
        typeToFunction.put(LocalDateTime.class, LocalDate::parse);
        typeToFunction.put(Long.class, Long::parseLong);
        typeToFunction.put(BigInteger.class, BigInteger::new);
        typeToFunction.put(Double.class, Double::new);

        return typeToFunction.getOrDefault(each.getType(), type -> {
            throw new UnsupportedOperationException("Type isn't supported by parser " + type);
        }).apply(value);
    }

    private <T> Table convertToTable(DataReadWriteSource<?> source) {
        if (source instanceof ConnectionReadWriteSource) {
            ConnectionReadWriteSource databaseSource = (ConnectionReadWriteSource) source;
            return new DatabaseRead().parseToTable(databaseSource);
        } else if (source instanceof FileReadWriteSource) {
            FileReadWriteSource fileSource = (FileReadWriteSource) source;
            return getStringParsingStrategy(fileSource).parseToTable(fileSource);
        } else {
            throw new UnsupportedOperationException("Unknown DataInputSource - " + source);
        }
    }

    private ParsingStrategy<FileReadWriteSource> getStringParsingStrategy(FileReadWriteSource fileSource) {
        String content = fileSource.getContent();
        char firstChar = content.charAt(0);

        switch (firstChar) {
            case '{':
            case '[':
                return new JsonRead();
            case '<':
                return new XmlRead();
            default:
                return new CsvRead();
        }
    }

    @Override
    public <T> void writeAll(DataReadWriteSource<?> content, List<T> object) {
        if (content instanceof ConnectionReadWriteSource) {
            new DatabaseWrite().write((ConnectionReadWriteSource) content, object);
        } else if (content instanceof FileReadWriteSource) {
            getWritingStrategy((FileReadWriteSource) object)
                    .write((FileReadWriteSource) content, object);
        } else {
            throw new UnsupportedOperationException("Unknown data input source");
        }

    }

    private WriteStrategy<FileReadWriteSource> getWritingStrategy(FileReadWriteSource object) {
        String content = FilenameUtils.getExtension(object.getSource().getName());
        switch (content) {
            case "json":
                new JsonWrite();
            case "xml":
                new XmlWrite();
            case "csv":
                new CsvWrite();
            default:
                throw new UnsupportedOperationException();
        }
    }
}
