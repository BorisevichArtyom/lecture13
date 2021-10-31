package by.itacademy.javaenterprise.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;


@Component
public class DataSourcee {

    @Autowired
    private HikariDataSource ds;
    private static DataSourcee dataSourcee;


    private DataSourcee() {
    }

    public static DataSourcee getDS() {
        if (dataSourcee == null) {
            synchronized (DataSourcee.class) {
                if (dataSourcee == null) {
                    dataSourcee = new DataSourcee();
                }
            }
        }

        return dataSourcee;
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}





