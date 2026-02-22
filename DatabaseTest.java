import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/rent_management";
        String username = "rentuser";
        String password = "rentpass";
        
        try {
            System.out.println("Attempting to connect to database...");
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("SUCCESS: Database connection established!");
            connection.close();
        } catch (SQLException e) {
            System.out.println("ERROR: Database connection failed!");
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}