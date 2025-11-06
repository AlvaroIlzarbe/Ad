package newspapercrud.dao.utilites;

import jakarta.annotation.PreDestroy;
import newspapercrud.common.Configuration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;



@Component
public class DBConnectionPool {



    private final DataSource hikariDataSource;

    public DBConnectionPool() {

        hikariDataSource = getHikariPool();
    }

    private DataSource getHikariPool() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(Configuration.getInstance().getProperty("dbUrl"));
        hikariConfig.setUsername(Configuration.getInstance().getProperty("user_name"));
        hikariConfig.setPassword(Configuration.getInstance().getProperty("password"));
        hikariConfig.setDriverClassName(Configuration.getInstance().getProperty("driver"));
        hikariConfig.setMaximumPoolSize(4);

        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);

        return new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() {
        Connection con=null;
        try {
            con = hikariDataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return con;
    }

    public void closeConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
@PreDestroy
    public void closePool() {
        ((HikariDataSource) hikariDataSource).close();
    }
}
