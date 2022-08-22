package org.example.strategy.read;

import org.example.model.Table;
import org.example.strategy.DataReadWriteSource;

public interface ParsingStrategy<T extends DataReadWriteSource> {

    Table parseToTable (T content);

}
