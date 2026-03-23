import dal.connection.ConnectionManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = ConnectionManager.getConnection()) {
            System.out.println("Database connection successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
