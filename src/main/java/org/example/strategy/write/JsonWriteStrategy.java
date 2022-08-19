package org.example.strategy.write;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.strategy.FileReadWriteSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@RequiredArgsConstructor
public class JsonWriteStrategy implements WriteStrategy{

    private final FileReadWriteSource source;

    @Override
    @SneakyThrows
    public void write(List<?> objects) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        String json = mapper.writeValueAsString(objects);
        Files.write(Path.of(source.getContent()), json.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }
}
