package repositroy.impl;

import model.Country;
import repositroy.inter.AbstractDao;
import repositroy.inter.CountryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static javax.management.remote.JMXConnectorFactory.connect;

public class CountryRepositoryImpl extends AbstractDao implements CountryRepository {
    public Country getCountry(ResultSet rs) throws SQLException {

        int id = rs.getInt("Id");
        String name = rs.getString("name");
        String nationality = rs.getString("nationality");

        Country contry = new Country(id, name, nationality);
        System.out.println(contry);
        return contry;

    }

    @Override
    public List<Country> getAll() {
        List<Country> list = new ArrayList<>();
        Connection conn;
        try {
            conn = connection();

            Statement stmt = conn.createStatement();
            stmt.execute("SELECT * FROM country");
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {

                Country contry = getCountry(rs);
                list.add(contry);

            }
        } catch (Exception ex) {

        }
        return list;
    }

    @Override
    public Country getById(int userId) {
        Country el = null;
        Connection conn;
        try {
            conn = connection();

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM country WHERE ID = ?");
            stmt.setInt(1, userId);
            stmt.execute();

            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {

                el = getCountry(rs);

            }
        } catch (Exception ex) {

        }
        return el;
    }

    @Override
    public boolean updateCountry(Country country) {
        String sql = "UPDATE country SET name = ?, nationality = ? WHERE id = ?";
        try (PreparedStatement stmt = connection().prepareStatement(sql)) {
            stmt.setString(1, country.getName());
            stmt.setString(2, country.getNationality());
            stmt.setInt(3, country.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean removeCountry(int id) {
        String sql = "DELETE FROM country WHERE id = ?";
        try (PreparedStatement stmt = connection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
