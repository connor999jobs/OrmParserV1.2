package org.example.strategy.write;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.model.Person;
import org.example.model.TableName;
import org.example.strategy.ConnectionReadWriteSource;
import org.example.utils.ConnectionToDatabase;

import java.sql.*;
import java.util.Iterator;
import java.util.List;


@RequiredArgsConstructor
public class DatabaseWriteStrategy implements WriteStrategy<ConnectionReadWriteSource> {


    @Override
    @SneakyThrows
    public <T> void write(List<T> objects) {
        ConnectionToDatabase connection = new ConnectionToDatabase();
        Class<?> tableClass = Person.class;
        String table = tableClass.getDeclaredAnnotation(TableName.class).name();

        String sql = String.format("insert into %1$s values (?,?,?,?,?)", table);

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            Iterator<T> iterator = objects.iterator();
            while (iterator.hasNext()) {
                Person person = (Person) iterator.next();
                statement.setString(1, person.getName());
                statement.setInt(2, person.getAge());
                statement.setDouble(3, person.getSalary());
                statement.setString(4, person.getPosition());
                statement.setObject(5, person.getDateOfBirth());
                statement.executeUpdate();
            }
            }

        }
    }

