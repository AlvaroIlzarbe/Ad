package newspapercrud.dao.utilites;

import newspapercrud.common.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Component
public class ConnectionDB {
    private final Configuration config;

    public ConnectionDB(Configuration config) {
        this.config = config;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config.getRequired("urlDB"),
                config.getRequired("user_name"),
                config.getRequired("password")
        );
    }
}
