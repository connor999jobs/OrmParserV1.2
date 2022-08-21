package org.example.strategy.write;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.model.Person;
import org.example.model.TableName;
import org.example.strategy.FileReadWriteSource;
import org.example.utils.ConnectionToDatabase;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@RequiredArgsConstructor
public class DatabaseWriteStrategy implements WriteStrategy{

    @Override
    @SneakyThrows
    public <T> void write(List<T> objects) {
        ConnectionToDatabase connection = new ConnectionToDatabase();
        String sql = "insert into person(name, age, salary, position, dateofbirth) VALUES (?,?,?,?,?)";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            Iterator<T> it = objects.iterator();
            while (it.hasNext()) {
                Person person = (Person) it.next();
                statement.setString(1, person.getName());
                statement.setInt(2, person.getAge());
                statement.setDouble(3, person.getSalary());
                statement.setString(4, person.getPosition());
                statement.setObject(5, person.getDateOfBirth()); // использую setObject для LocalDate;
                statement.executeUpdate();
            }
        }
    }
}