package repositroy.inter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDao {
    public  Connection connection() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/resumeappdb";
        String username ="root";
        String password = "15466451";
        Connection c = DriverManager.getConnection(url,username,password);
        return c;
    }
}
