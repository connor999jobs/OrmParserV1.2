package org.example.strategy.write;

import java.util.List;

public interface WriteStrategy {
    <T> void write(List<T> objects);
}
