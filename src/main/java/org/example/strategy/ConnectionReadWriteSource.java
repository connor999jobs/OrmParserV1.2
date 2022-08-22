package org.example.strategy;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.model.TableName;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RequiredArgsConstructor
@Getter
public class ConnectionReadWriteSource implements DataReadWriteSource{

    private final Connection source;

    private final Class<?> tableClass;

    public String getTableFromAnnotations(){
        return tableClass.getDeclaredAnnotation(TableName.class).name();
    }

    @Override
    @SneakyThrows
    public ResultSet getContent() {
        Statement statement = source.createStatement();
        return statement.executeQuery("select * from " + getTableFromAnnotations());
    }
}
