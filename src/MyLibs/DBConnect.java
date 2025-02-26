package MyLibs;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    //all required fields to connect to db
    private final static String url = "jdbc:mysql://localhost:3306/sql_lotsdb";
    private final static String username = "root";
    private final static String password = "root";
    
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url, username, password);
    }
}
