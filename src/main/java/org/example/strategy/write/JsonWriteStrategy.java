package org.example.strategy.write;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.strategy.FileReadWriteSource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@RequiredArgsConstructor
public class JsonWriteStrategy implements WriteStrategy{

    private final FileReadWriteSource source;

    @Override
    @SneakyThrows
    public <T> void write(List<T> objects) {
        File file = source.getSource();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        mapper.writeValue(file, objects);
    }
}
