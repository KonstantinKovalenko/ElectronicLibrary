package logic;

import javax.sql.DataSource;
import logic.database.JDBCConnectionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@ComponentScan(basePackages = "logic")
public class Configuration {

    @Autowired
    private JDBCConnectionProperties cProperties;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://"
                + cProperties.getHost()
                + ":" + cProperties.getPort()
                + "/" + cProperties.getDBName()+"?useUnicode=true&characterEncoding=UTF-8");
        dataSource.setUsername(cProperties.getUserName());
        dataSource.setPassword(cProperties.getPassword());
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
