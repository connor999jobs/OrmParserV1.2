package org.example.strategy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Getter
public class FileReadWriteSource implements DataReadWriteSource{

    private final File source;

    @Override
    @SneakyThrows
    public String getContent() {
        return FileUtils.readFileToString(source, StandardCharsets.UTF_8);
    }
}
