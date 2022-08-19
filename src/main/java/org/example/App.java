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
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {

    private static final Orm ORM = new OrmImpl();
    private static final ConnectionToDatabase connect = new ConnectionToDatabase();


    public static void main( String[] args ) throws URISyntaxException {

        List<Person> result = new ArrayList<>();
//        DataReadWriteSource<ResultSet> rw = new ConnectionReadWriteSource(connect.getConnection());
//        result = ORM.readAll(rw,Person.class);


        URL url = App.class.getClassLoader().getResource("reader.json");
        DataReadWriteSource<?> readWriteSource = new FileReadWriteSource(new File(url.toURI()));
        result.add(new Person("V",22,2222,"Marketing","27-09-1998"));
        ORM.writeAll(readWriteSource, result);

    }
}
