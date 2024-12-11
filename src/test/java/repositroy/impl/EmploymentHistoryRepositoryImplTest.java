package repositroy.impl;

import io.github.cdimascio.dotenv.Dotenv;
import model.EmploymentHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EmploymentHistoryRepositoryImplTest {

    private Connection connection;
    private EmploymentHistoryRepositoryImpl employmentHistoryRepository;

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
            stmt.execute("DROP TABLE IF EXISTS employment_history;");
        }

        String createEmploymentHistoryTable = "CREATE TABLE IF NOT EXISTS employment_history (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "header VARCHAR(255) NOT NULL, " +
                "job_description TEXT, " +
                "begin_date DATE, " +
                "end_date DATE, " +
                "user_id INT NOT NULL)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createEmploymentHistoryTable);
        }

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO employment_history (header, job_description, begin_date, end_date, user_id) VALUES (?, ?, ?, ?, ?), (?, ?, ?, ?, ?);")) {
            stmt.setString(1, "Software Developer");
            stmt.setString(2, "Developed enterprise applications.");
            stmt.setDate(3, Date.valueOf("2020-01-01"));
            stmt.setDate(4, Date.valueOf("2022-12-31"));
            stmt.setInt(5, 1);

            stmt.setString(6, "Project Manager");
            stmt.setString(7, "Managed software projects.");
            stmt.setDate(8, Date.valueOf("2018-01-01"));
            stmt.setDate(9, Date.valueOf("2019-12-31"));
            stmt.setInt(10, 1);

            stmt.executeUpdate();
        }

        employmentHistoryRepository = new EmploymentHistoryRepositoryImpl() {
            @Override
            public Connection connection() {
                return connection;
            }
        };
    }

    @Test
    void testGetAllEmploymentHistoryByUserId() {
        List<EmploymentHistory> employmentHistories = employmentHistoryRepository.getAllEmploymentHistoryByUserId(1);

        assertNotNull(employmentHistories);
        assertEquals(2, employmentHistories.size());

        EmploymentHistory firstJob = employmentHistories.get(0);
        assertEquals("Software Developer", firstJob.getHeader());
        assertEquals("Developed enterprise applications.", firstJob.getJobDescription());
        assertEquals(Date.valueOf("2020-01-01"), firstJob.getBeginDate());
        assertEquals(Date.valueOf("2022-12-31"), firstJob.getEndDate());
        assertEquals(1, firstJob.getUser().getId());

        EmploymentHistory secondJob = employmentHistories.get(1);
        assertEquals("Project Manager", secondJob.getHeader());
        assertEquals("Managed software projects.", secondJob.getJobDescription());
        assertEquals(Date.valueOf("2018-01-01"), secondJob.getBeginDate());
        assertEquals(Date.valueOf("2019-12-31"), secondJob.getEndDate());
        assertEquals(1, secondJob.getUser().getId());
    }
}
