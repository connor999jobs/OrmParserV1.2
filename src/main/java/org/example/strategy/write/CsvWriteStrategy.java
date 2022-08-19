package org.example.strategy.write;

import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.strategy.FileReadWriteSource;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


@RequiredArgsConstructor
public class CsvWriteStrategy implements WriteStrategy{

    private final FileReadWriteSource source;


    @Override
    @SneakyThrows
    public void write(List<?> objects) {
        File file = source.getSource();
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        String[] names = getNamesFromEntity(objects);
        writer.writeAll(convertEntitiesToList(names, objects));
        writer.flush();
        writer.close();
    }

    private List<String[]> convertEntitiesToList(String[] names, List<?> objects) {
        List<String[]> list =new ArrayList<>();
        list.add(names);
        IntStream.range(0, objects.size())
                .boxed()
                .forEach(integer -> list.add(createArrayFromEntity(names,objects.get(integer))));
        return list;
    }

    private String[] createArrayFromEntity(String[] names, Object object) {
        return Arrays.stream(names)
                .map(name -> getValues(object, name))
                .toArray(String[]::new);
    }

    @SneakyThrows
    private Object getValues(Object object, String name) {
        Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return String.valueOf(field.get(object));
    }

    private String[] getNamesFromEntity(List<?> objects) {
        return Arrays.stream(objects.get(0).getClass().getDeclaredFields())
                .map(Field::getName)
                .toArray(String[]::new);
    }
}
