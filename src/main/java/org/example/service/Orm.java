package org.example.service;

import lombok.SneakyThrows;
import org.example.strategy.DataReadWriteSource;

import java.util.List;

public interface Orm {
    @SneakyThrows
    <T> List<T> readAll(DataReadWriteSource<?> source, Class<T> cls);

    <T> void writeAll(DataReadWriteSource<?> content, List<T> object);
}
