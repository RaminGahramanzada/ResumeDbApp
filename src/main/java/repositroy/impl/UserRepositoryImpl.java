package repositroy.impl;

import model.Nationality;
import model.User;
import repositroy.inter.AbstractDao;
import repositroy.inter.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepositoryImpl extends AbstractDao implements UserRepository {

    private User getUser(ResultSet rs) throws SQLException {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String phone = rs.getString("phone");
            String email = rs.getString("email");
            int nationalityId = rs.getInt("nationality_id");
            int birthPlaceId = rs.getInt("birthplace_id");
            String nationalityStr = rs.getString("nationality");
            String birthPlaceStr = rs.getString("birthplace");
            Date birthDate = rs.getDate("birthdate");

            Nationality nationality = new Nationality(nationalityId,nationalityStr,null);
            Nationality birthPlace = new Nationality(birthPlaceId,birthPlaceStr,null);

           return new User(id, name, surname, phone, email,birthDate,nationality,birthPlace);
    }

    @Override
    public List<User> getAll() {
        List<User> result = new ArrayList<>();

        try (Connection c = connection()) {
            Statement stmt = c.createStatement();
            stmt.execute("SELECT"
                    + "u.*,"
                    + "n.name as nationality,"
                    + "c.country_name as birthplace"
                    + "FROM `user` u"
                    + "LEFT JOIN nationality n ON u.nationality_id = n.id"
                    + "LEFT JOIN nationality c ON u.birthplace_id = c.id");
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
               User u = getUser(rs);
               result.add(u);
            }
        } catch (Exception ex) {
            Logger.getLogger(UserRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }


    @Override
    public boolean updateUser(User user) {
        try (Connection c = connection()){
            PreparedStatement stmt = c.prepareStatement("update user set name=?,surname=?,phone=?,email=? where id=?");
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getEmail());
            stmt.setInt(5, user.getId());

            return stmt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeUser(int id) {
        try {
            Connection c = connection();
            Statement stmt = c.createStatement();
            stmt.execute("delete from user  where id = "+id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public User getById(int userId) {
        User result = null;

        try (Connection c = connection()) {
            Statement stmt = c.createStatement();
            stmt.execute("SELECT"
                   + "u.*,"
                   + "n.name as nationality,"
                   + "c.country_name as birthplace"
                   + "FROM `user` u"
                   + "LEFT JOIN nationality n ON u.nationality_id = n.id"
                   + "LEFT JOIN nationality c ON u.birthplace_id = c.id where u.id="+userId);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
               result = getUser(rs);
            }
        } catch (Exception ex) {
            Logger.getLogger(UserRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean addUser(User user) {
        try (Connection c = connection()){
            PreparedStatement stmt = c.prepareStatement("insert into  user (name,surname,phone,email) values (?,?,?,?)");
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getEmail());

            return stmt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


}
