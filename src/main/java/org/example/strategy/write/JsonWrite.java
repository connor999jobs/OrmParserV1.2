package org.example.strategy.write;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import lombok.SneakyThrows;
import org.example.strategy.FileReadWriteSource;

import java.io.File;
import java.util.List;

public class JsonWrite implements WriteStrategy<FileReadWriteSource> {

    @Override
    @SneakyThrows
    public void write(FileReadWriteSource source, List<?> list) {
        File file = source.getSource();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        mapper.writeValue(file, list);
    }
}
