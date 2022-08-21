package org.example.strategy.write;

import org.example.strategy.ConnectionReadWriteSource;

import java.util.List;

public interface WriteStrategy<T extends ConnectionReadWriteSource> {
    <T> void write(List<T> objects);
}
