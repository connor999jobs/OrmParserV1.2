package org.example.strategy.write;

import java.util.List;

public interface WriteStrategy {
    void write(List<?> objects);
}
