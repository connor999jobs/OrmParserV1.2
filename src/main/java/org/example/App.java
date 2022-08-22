package org.example;


import org.example.model.Person;
import org.example.service.Orm;
import org.example.service.impl.OrmImpl;
import org.example.strategy.ConnectionReadWriteSource;
import org.example.strategy.DataReadWriteSource;
import org.example.utils.ConnectionToDB;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class App {

    private static final ConnectionToDB connect = new ConnectionToDB();
    private static final Orm OrmSystem = new OrmImpl();
    public static void main(String[] args) {

        List<Person> list;
        DataReadWriteSource<ResultSet> rw = new ConnectionReadWriteSource(connect.getConnection(), Person.class);
        list = OrmSystem.readAll(rw, Person.class);

        LocalDate date = LocalDate.of(1914, 7, 28);
        list.add(new Person("Valik",23,5000.0,"senior", date));
        OrmSystem.writeAll(rw,list);
    }
}

