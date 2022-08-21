package org.example.strategy.write;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.strategy.ConnectionReadWriteSource;
import org.example.strategy.FileReadWriteSource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@RequiredArgsConstructor
public class XmlWriteStrategy implements WriteStrategy<ConnectionReadWriteSource>{

    public final FileReadWriteSource source;

    @Override
    @SneakyThrows
    public <T> void write(List<T> objectsList) {
        File file = source.getSource();
        XmlMapper mapper = new XmlMapper();
        mapper.registerModule(new JSR310Module());
        mapper.writeValue(file, objectsList);
    }
}
