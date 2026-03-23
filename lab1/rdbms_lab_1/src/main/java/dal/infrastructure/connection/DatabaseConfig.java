package dal.infrastructure.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DatabaseConfig {

    private static final String PROPERTIES_FILE = "db.properties";

    private final String url;
    private final String username;
    private final String password;

    private DatabaseConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DatabaseConfig load() {
        Properties properties = new Properties();

        try (InputStream inputStream = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {

            if (inputStream == null) {
                throw new RuntimeException("File " + PROPERTIES_FILE + " not found in resources");
            }

            properties.load(inputStream);

            String url = properties.getProperty("jdbc.url");
            String username = properties.getProperty("jdbc.username");
            String password = properties.getProperty("jdbc.password");

            if (url == null || username == null || password == null) {
                throw new RuntimeException("Database properties are missing in " + PROPERTIES_FILE);
            }

            return new DatabaseConfig(url, username, password);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
