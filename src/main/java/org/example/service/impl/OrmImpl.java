package org.example.service.impl;

import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.example.model.Table;
import org.example.service.Orm;
import org.example.strategy.ConnectionReadWriteSource;
import org.example.strategy.DataReadWriteSource;
import org.example.strategy.FileReadWriteSource;
import org.example.strategy.read.*;
import org.example.strategy.write.CsvWriteStrategy;
import org.example.strategy.write.JsonWriteStrategy;
import org.example.strategy.write.WriteStrategy;
import org.example.strategy.write.XmlWriteStrategy;

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
    @SneakyThrows
    public <T> List<T> readAll(DataReadWriteSource<?> inputSource, Class<T> cls) {
        Table table = convertToTable(inputSource, cls);
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

    private static Object transformValueToFieldType(Field field, String value) {
        Map<Class<?>, Function<String, Object>> typeToFunction = new LinkedHashMap<>();
        typeToFunction.put(String.class, s -> s);
        typeToFunction.put(Integer.class, Integer::parseInt);
        typeToFunction.put(Float.class, Float::parseFloat);
        typeToFunction.put(LocalDate.class, LocalDate::parse);
        typeToFunction.put(LocalDateTime.class, LocalDate::parse);
        typeToFunction.put(Long.class, Long::parseLong);
        typeToFunction.put(BigInteger.class, BigInteger::new);
        typeToFunction.put(Double.class, Double::new);

        return typeToFunction.getOrDefault(field.getType(), type -> {
            throw new UnsupportedOperationException("Type isn't supported by parser " + type);
        }).apply(value);
    }

    private Table convertToTable(DataReadWriteSource dataInputSource, Class<?> cls) {
        if (dataInputSource instanceof ConnectionReadWriteSource) {
            ConnectionReadWriteSource databaseSource = (ConnectionReadWriteSource) dataInputSource;

            return new DatabaseParsingStrategy(cls).parseToTable(databaseSource);
        } else if (dataInputSource instanceof FileReadWriteSource) {
            FileReadWriteSource fileSource = (FileReadWriteSource) dataInputSource;
            return getStringParsingStrategy(fileSource).parseToTable(fileSource);
        } else {
            throw new UnsupportedOperationException("Unknown DataInputSource " + dataInputSource);
        }
    }

    private ParsingStrategy<FileReadWriteSource> getStringParsingStrategy(FileReadWriteSource inputSource) {
        String content = inputSource.getContent();
        char firstChar = content.charAt(0);

        switch (firstChar) {
            case '{':
            case '[':
                return new JSONParsingStrategy();
            case '<':
                return new XMLParsingStrategy();
            default:
                return new CSVParsingStrategy();
        }
    }


    @Override
    public <T> void writeAll(DataReadWriteSource<?> content, List<T> object) {
        if (content instanceof FileReadWriteSource){
            WriteStrategy strategy = writeStrategy(content);
            writeValueToSource(strategy,object);
        }

    }

    private <T> void writeValueToSource(WriteStrategy strategy, List<T> object) {
        strategy.write(object);
    }

    private WriteStrategy writeStrategy(DataReadWriteSource<?> content) {
        String ext = FilenameUtils.getExtension(((FileReadWriteSource) content).getSource().getName());
        if (ext.equals("json")) {
            return new JsonWriteStrategy((FileReadWriteSource) content);
        }else if(ext.equals("xml")){
            return new XmlWriteStrategy((FileReadWriteSource) content);
        }else {
            return new CsvWriteStrategy((FileReadWriteSource) content);
        }
    }
}
