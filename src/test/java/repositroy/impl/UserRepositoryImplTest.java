package repositroy.impl;

import io.github.cdimascio.dotenv.Dotenv;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {

    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() throws Exception {
        Dotenv dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .filename("config.env")
                .load();

        String url = dotenv.get("DB_URL");
        String username = dotenv.get("DB_USERNAME");
        String password = dotenv.get("DB_PASSWORD");

        userRepository = new UserRepositoryImpl() {
            @Override
            public Connection connection() throws SQLException {
                return DriverManager.getConnection(url, username, password);
            }
        };

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement stmt = connection.createStatement();

            stmt.execute("CREATE TABLE IF NOT EXISTS user (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(50), " +
                    "surname VARCHAR(50), " +
                    "phone VARCHAR(15), " +
                    "email VARCHAR(50), " +
                    "birthdate DATE, " +
                    "nationality_id INT, " +
                    "birthplace_id INT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS country (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(50), " +
                    "nationality VARCHAR(50))");

            stmt.execute("DELETE FROM user");
            stmt.execute("DELETE FROM country");

            stmt.execute("INSERT INTO country (id, name, nationality) VALUES (1, 'USA', 'American'), (2, 'France', 'French')");
            stmt.execute("INSERT INTO user (id, name, surname, phone, email, birthdate, nationality_id, birthplace_id) " +
                    "VALUES (1, 'Harry', 'Kane', '123456789', 'noMore.Cup@forKane.com', '1990-01-01', 1, 2)");
        }
    }

    @Test
    void getAll() {
        List<User> users = userRepository.getAll();
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("Harry",users.get(0).getName());
    }

    @Test
    void updateUser() {
        User user = userRepository.getById(1);
        assertNotNull(user);
        user.setPhone("12356789");
        boolean isUpdated = userRepository.updateUser(user);
        assertTrue(isUpdated);

        User updatedUser = userRepository.getById(1);
        assertEquals("12356789",updatedUser.getPhone());
    }

    @Test
    void removeUser() {
        boolean isRemoved = userRepository.removeUser(1);
        assertTrue(isRemoved);
        User user = userRepository.getById(1);
        assertNull(user);
    }

    @Test
    void getById() {
        User user = userRepository.getById(1);
        assertNotNull(user);
        assertEquals("Harry",user.getName());
    }

    @Test
    void addUser() {
        User newUser = new User(2,"Pulisic","Mate","987654321","pulisic@Mate.com",
                new java.sql.Date(1992, 5, 10), null, null);
        boolean isAdded = userRepository.addUser(newUser);

        List<User> users = userRepository.getAll();
        assertEquals(2,users.size());
    }


}