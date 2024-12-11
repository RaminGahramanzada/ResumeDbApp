package repositroy.impl;

import static org.junit.jupiter.api.Assertions.*;

import model.UserSkill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.List;
import io.github.cdimascio.dotenv.Dotenv;

class UserSkillRepositoryImplTest {

    private Connection connection;
    private UserSkillRepositoryImpl userSkillRepository;

    @BeforeEach
    void setUp() throws SQLException {
        Dotenv dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .filename("config.env")
                .load();

        String url = dotenv.get("DB_URL");
        String username = dotenv.get("DB_USERNAME");
        String password = dotenv.get("DB_PASSWORD");

        connection = DriverManager.getConnection(url, username, password);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS user_skill;");
            stmt.execute("DROP TABLE IF EXISTS user;");
            stmt.execute("DROP TABLE IF EXISTS skill;");
        }

        String createSkillTable = "CREATE TABLE IF NOT EXISTS skill (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL);";

        String createUserTable = "CREATE TABLE IF NOT EXISTS user (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255));";

        String createUserSkillTable = "CREATE TABLE IF NOT EXISTS user_skill (" +
                "user_id INT, " +
                "skill_id INT, " +
                "power INT, " +
                "PRIMARY KEY (user_id, skill_id), " +
                "FOREIGN KEY (user_id) REFERENCES user(id), " +
                "FOREIGN KEY (skill_id) REFERENCES skill(id));";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createSkillTable);
            stmt.execute(createUserTable);
            stmt.execute(createUserSkillTable);
        }

        int javaSkillId;
        int pythonSkillId;
        int userId;

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO skill (name) VALUES (?);", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Java");
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                javaSkillId = rs.getInt(1);
            }

            stmt.setString(1, "Python");
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                pythonSkillId = rs.getInt(1);
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO user (name) VALUES (?);", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Manuel Neuer");
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                userId = rs.getInt(1);
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO user_skill (user_id, skill_id, power) VALUES (?, ?, ?), (?, ?, ?);")) {
            stmt.setInt(1, userId);
            stmt.setInt(2, javaSkillId);
            stmt.setInt(3, 90);
            stmt.setInt(4, userId);
            stmt.setInt(5, pythonSkillId);
            stmt.setInt(6, 80);
            stmt.executeUpdate();
        }

        userSkillRepository = new UserSkillRepositoryImpl() {
            @Override
            public Connection connection() {
                return connection;
            }
        };
    }

    @Test
    void testGetAllSkillByUserId() {
        List<UserSkill> skills = userSkillRepository.getAllSkillByUserId(1);

        assertNotNull(skills);
        assertEquals(2, skills.size());

        UserSkill javaSkill = skills.get(0);
        assertEquals("Java", javaSkill.getSkill().getName());
        assertEquals(90, javaSkill.getPower());

        UserSkill pythonSkill = skills.get(1);
        assertEquals("Python", pythonSkill.getSkill().getName());
        assertEquals(80, pythonSkill.getPower());
    }
}