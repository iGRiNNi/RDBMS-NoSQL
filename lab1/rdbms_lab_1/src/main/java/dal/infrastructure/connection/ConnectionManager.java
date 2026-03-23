package dal.infrastructure.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionManager {

    private static final DatabaseConfig CONFIG = DatabaseConfig.load();

    private ConnectionManager() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    CONFIG.getUrl(),
                    CONFIG.getUsername(),
                    CONFIG.getPassword()
            );
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get database connection", e);
        }
    }
}
