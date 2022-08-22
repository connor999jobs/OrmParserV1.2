package org.example.strategy.write;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.strategy.ConnectionReadWriteSource;
import org.example.utils.ModelSqlHelper;

import java.sql.PreparedStatement;
import java.util.List;

@RequiredArgsConstructor
public class DatabaseWrite implements WriteStrategy<ConnectionReadWriteSource> {

    @Override
    @SneakyThrows
    public void write(ConnectionReadWriteSource source, List<?> list) {
        for (Object values : list){
            ModelSqlHelper helper = new ModelSqlHelper(ModelSqlHelper.getMetaInformation(source));
            String sql = helper.buildSQL(values);
            PreparedStatement st = source.getSource().prepareStatement(sql);
            helper.bindArguments(values, st);
            st.executeUpdate();
        }
    }
}
