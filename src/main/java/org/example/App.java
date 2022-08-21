package org.example;

import org.example.model.Person;
import org.example.service.Orm;
import org.example.service.impl.OrmImpl;
import org.example.strategy.ConnectionReadWriteSource;
import org.example.strategy.DataReadWriteSource;
import org.example.strategy.FileReadWriteSource;
import org.example.utils.ConnectionToDatabase;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {

    private static final Orm ORM = new OrmImpl();
    private static final ConnectionToDatabase connect = new ConnectionToDatabase();


    public static void main( String[] args ) throws URISyntaxException {

        List<Person> result;
        DataReadWriteSource<ResultSet> rw = new ConnectionReadWriteSource(connect.getConnection());
        result = ORM.readAll(rw,Person.class);
        LocalDate date = LocalDate.of(1914, 7, 28);
        result.add(new Person("Va",20,200.0,"pp",date));
        ORM.writeAll(rw, result);


//        URL url = App.class.getClassLoader().getResource("reader.csv");
//        DataReadWriteSource<?> readWriteSource = new FileReadWriteSource(new File(url.toURI()));
//        result = ORM.readAll(readWriteSource, Person.class);
//        LocalDate date = LocalDate.of(1914, 7, 28);
//        result.add(new Person("Va",20,200.0,"pp", date));
//        ORM.writeAll(readWriteSource, result);

    }
}
