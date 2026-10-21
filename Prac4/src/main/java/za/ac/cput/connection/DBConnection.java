
package za.ac.cput.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    /**
     *
     * @return @throws SQLException
     */
    public static Connection derbyConnection() throws SQLException {
        String dbURL = "jdbc:derby://localhost:1527/UniversityTwo";
        String username = "administrator";
        String password = "admin";

        System.out.println("About to get a connection....");
        Connection connection = DriverManager.getConnection(dbURL, username, password);
        System.out.println("Connection Established Successfully....");
        return connection;
    }
}// end of class
