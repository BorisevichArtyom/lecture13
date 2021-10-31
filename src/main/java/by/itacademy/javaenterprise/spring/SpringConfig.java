package by.itacademy.javaenterprise.spring;

import by.itacademy.javaenterprise.Main;
import by.itacademy.javaenterprise.command.Command;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("by.itacademy.javaenterprise")
@PropertySource("classpath:database.properties")
public class SpringConfig {


    @Bean
    public HikariConfig hikariConfig(@Value("${datasource.jdbcUrl}") String url,
                                     @Value("${dataSource.user}") String userName,
                                     @Value("${dataSource.password}") String password,
                                     @Value("${dataSource.maxPoolSize}") Integer maxPoolSize,
                                     @Value("${dataSource.cachePrepStmts}") String cachePrepStmts,
                                     @Value("${dataSource.prepStmtCacheSize}") String prepStmtCacheSize,
                                     @Value("${dataSource.prepStmtCacheSqlLimit}") String prepStmtCacheSqlLimit) {
        HikariConfig hconfig = new HikariConfig();
        hconfig.setJdbcUrl(url);
        hconfig.setUsername(userName);
        hconfig.setPassword(password);
        hconfig.setMaximumPoolSize(maxPoolSize);
        hconfig.addDataSourceProperty("cachePrepStmts", cachePrepStmts);
        hconfig.addDataSourceProperty("prepStmtCacheSize", prepStmtCacheSize);
        hconfig.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);
        return hconfig;
    }

    @Bean
    public HikariDataSource hikariDataSource(@Autowired HikariConfig hikariConfig) {

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public Main qualifierExample(@Autowired
                                 @Qualifier("UpdateTraining") Command<Integer> command) {
        return new Main(command);
    }
}
