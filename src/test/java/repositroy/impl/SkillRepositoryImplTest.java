package repositroy.impl;

import io.github.cdimascio.dotenv.Dotenv;
import model.Skill;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositroy.inter.SkillRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SkillRepositoryImplTest {
    private SkillRepository skillRepository;
    private Connection connection;

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

        String createTableSQL = "CREATE TABLE IF NOT EXISTS skill ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        }

        clearDatabase();

        String insertSQL = "INSERT INTO skill (name) VALUES ('Java'), ('Python');";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(insertSQL);
        }

        skillRepository = new SkillRepositoryImpl();
    }

    @Test
    void testInsertSkill() {
        Skill newSkill = new Skill();
        newSkill.setName("JavaScript");
        boolean isInserted = skillRepository.insertSkill(newSkill);

        assertTrue(isInserted, "Skill should be inserted successfully");
        assertNotNull(newSkill.getId(), "The skill ID should be generated");
    }

    @Test
    void testGetAllSkills() {
        List<Skill> skills = skillRepository.getAll();

        assertNotNull(skills, "The skills list should not be null");
        assertFalse(skills.isEmpty(), "The skills list should not be empty");
    }

    @Test
    void testGetSkillById() {
        Skill skill = skillRepository.getById(1); // Assuming ID 1 exists

        assertNotNull(skill, "Skill should not be null");
        assertEquals(1, skill.getId(), "Skill ID should be 1");
        assertEquals("Updated Skill Name", skill.getName(), "Skill name should be Java");
    }

    @Test
    void testUpdateSkill() {
        Skill existingSkill = skillRepository.getById(1); // Assuming ID 1 exists
        existingSkill.setName("Updated Skill Name");
        boolean isUpdated = skillRepository.updateSkill(existingSkill);

        assertTrue(isUpdated, "Skill should be updated successfully");

        Skill updatedSkill = skillRepository.getById(1);
        assertEquals("Updated Skill Name", updatedSkill.getName(), "The skill name should be updated");
    }

    @Test
    void testRemoveSkill() {
        Skill skillToDelete = new Skill();
        skillToDelete.setName("To Be Deleted");
        skillRepository.insertSkill(skillToDelete);
        int idToDelete = skillToDelete.getId();
        boolean isDeleted = skillRepository.removeSkill(idToDelete);

        assertTrue(isDeleted, "Skill should be removed successfully");

        Skill deletedSkill = skillRepository.getById(idToDelete);
        assertNull(deletedSkill, "The deleted skill should not exist in the database");
    }

    @Test
    void testGetSkillsByName() {
        List<Skill> skills = skillRepository.getByName("Java");
        assertNotNull(skills, "Skills list should not be null");
        assertTrue(skills.size() > 0, "There should be at least one skill matching the name");
    }

    @AfterEach
    void tearDown() throws SQLException {
        clearDatabase();
    }
    private void clearDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM skill");
        }
    }
}

