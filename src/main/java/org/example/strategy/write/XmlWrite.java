package org.example.strategy.write;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import lombok.SneakyThrows;
import org.example.strategy.FileReadWriteSource;

import java.io.File;
import java.util.List;

public class XmlWrite implements WriteStrategy<FileReadWriteSource> {

    @SneakyThrows
    @Override
    public void write(FileReadWriteSource source, List<?> list) {
        File file = source.getSource();
        XmlMapper mapper = new XmlMapper();
        mapper.registerModule(new JSR310Module());
        mapper.writeValue(file, list);
    }
}
