package repositroy.impl;

import io.github.cdimascio.dotenv.Dotenv;
import model.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.List;


class CountryRepositoryImplTest {

    private Connection connection;
    private CountryRepositoryImpl countryRepository;

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
            stmt.execute("DROP TABLE IF EXISTS country;");
        }

        String createCountryTable = "CREATE TABLE IF NOT EXISTS country (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "nationality VARCHAR(255) NOT NULL);";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createCountryTable);
        }

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO country (name, nationality) VALUES (?, ?), (?, ?);")) {
            stmt.setString(1, "USA");
            stmt.setString(2, "American");
            stmt.setString(3, "France");
            stmt.setString(4, "French");
            stmt.executeUpdate();
        }

        countryRepository = new CountryRepositoryImpl() {
            @Override
            public Connection connection() {
                return connection;
            }
        };
    }

    @Test
    void testGetAll() {
        List<Country> countries = countryRepository.getAll();

        assertNotNull(countries);
        assertEquals(2, countries.size());

        Country usa = countries.get(0);
        assertEquals("USA", usa.getName());
        assertEquals("American", usa.getNationality());

        Country france = countries.get(1);
        assertEquals("France", france.getName());
        assertEquals("French", france.getNationality());
    }

    @Test
    void testGetById() {
        Country country = countryRepository.getById(1);

        assertNotNull(country);
        assertEquals("USA", country.getName());
        assertEquals("American", country.getNationality());
    }

    @Test
    void testUpdateCountry() {
        Country country = new Country(1, "United States", "American");
        boolean updated = countryRepository.updateCountry(country);

        assertTrue(updated);

        Country updatedCountry = countryRepository.getById(1);
        assertEquals("United States", updatedCountry.getName());
        assertEquals("American", updatedCountry.getNationality());
    }

    @Test
    void testRemoveCountry() {
        boolean removed = countryRepository.removeCountry(1);

        assertTrue(removed);

        Country country = countryRepository.getById(1);
        assertNull(country);
    }
}
