package repositroy.inter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDao {
    public  Connection connection() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/yourdb";
        String username ="root";
        String password = "yourpassword";
        Connection c = DriverManager.getConnection(url,username,password);
        return c;
    }
}
