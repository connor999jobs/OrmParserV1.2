package org.example.strategy.write;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.strategy.FileReadWriteSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@RequiredArgsConstructor
public class XmlWriteStrategy implements WriteStrategy{

    public final FileReadWriteSource source;

    @Override
    @SneakyThrows
    public void write(List<?> objectsList) {
        XmlMapper mapper = new XmlMapper();
        mapper.registerModule(new JSR310Module());
        String xmlValue = mapper.writeValueAsString(objectsList);
        Files.write(Path.of(source.getContent()), xmlValue.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }
}
