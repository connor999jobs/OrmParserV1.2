package org.example;


import com.sun.tools.javac.Main;
import org.example.model.Person;
import org.example.service.Orm;
import org.example.service.impl.OrmImpl;
import org.example.strategy.ConnectionReadWriteSource;
import org.example.strategy.DataReadWriteSource;
import org.example.strategy.FileReadWriteSource;
import org.example.utils.ConnectionToDB;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final ConnectionToDB connect = new ConnectionToDB();
    private static final Orm OrmSystem = new OrmImpl();


    public static void main(String[] args) throws URISyntaxException {

        LocalDate date = LocalDate.of(1914, 7, 28);


        URL csvUrl = Main.class.getClassLoader().getResource("reader.csv");
        DataReadWriteSource<?> readCsv = new FileReadWriteSource(new File(csvUrl.toURI()));
        List<Person> csvList = OrmSystem.readAll(readCsv, Person.class);

        csvList.add(new Person("Valik",23,5000.0,"senior", date));
        OrmSystem.writeAll(readCsv,csvList);



        URL jsonUrl = Main.class.getClassLoader().getResource("reader.json");
        DataReadWriteSource<?> readJson = new FileReadWriteSource(new File(jsonUrl.toURI()));
        List<Person> jsonList = OrmSystem.readAll(readJson, Person.class);

        csvList.add(new Person("Valik",23,5000.0,"senior", date));
        OrmSystem.writeAll(readJson,jsonList);


        URL xmlUrl = Main.class.getClassLoader().getResource("reader.xml");
        DataReadWriteSource<?> readXml = new FileReadWriteSource(new File(xmlUrl.toURI()));
        List<Person> xmlList = OrmSystem.readAll(readXml, Person.class);

        csvList.add(new Person("Valik",23,5000.0,"senior", date));
        OrmSystem.writeAll(readXml,xmlList);


        ConnectionReadWriteSource readDataSource = new ConnectionReadWriteSource(connect.getConnection(), Person.class);
        List<Person> databaseList = OrmSystem.readAll(readDataSource, Person.class);

        databaseList.add(new Person("Valik",23,5000.0,"senior", date));
        OrmSystem.writeAll(readDataSource,databaseList);


    }
}

