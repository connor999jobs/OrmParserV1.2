package org.example.strategy.write;

import org.example.strategy.DataReadWriteSource;

import java.util.List;

public interface WriteStrategy<T extends DataReadWriteSource> {

    void write(T source, List<?> list);
}
