package repositroy.impl;

import model.Skill;
import repositroy.inter.AbstractDao;
import repositroy.inter.SkillRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SkillRepositoryImpl extends AbstractDao implements SkillRepository {

    @Override
    public List<Skill> getAll() {
        List<Skill> list = new ArrayList<>();
        Connection conn;
        try {
            conn = connection();

            Statement stmt = conn.createStatement();
            stmt.execute("SELECT * FROM skill");
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {

                int id = rs.getInt("Id");
                String name = rs.getString("name");
                list.add(new Skill(id, name));

            }
        } catch (Exception ex) {

        }
        return list;
    }

    @Override
    public Skill getById(int userId) {
        Skill usr = null;
        Connection conn;
        try {
            conn = connection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM skill WHERE ID = ?");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("name");

                usr = new Skill(id, name);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return usr;
    }

    @Override
    public boolean updateSkill(Skill u) {
        Connection conn;
        boolean b = true;
        try {
            conn = connection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE skill SET name=? WHERE id= ?");
            stmt.setString(1, u.getName());
            stmt.setInt(2, u.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                b = true;
            } else {
                b = false;
            }

        } catch (Exception ex) {
            System.err.println(ex);
            b = false;
        }
        return b;
    }

    public boolean insertSkill(Skill skl) {
        Connection conn;
        boolean b = true;
        try {
            conn = connection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO skill (name) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, skl.getName());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    skl.setId(generatedKeys.getInt(1));
                }
                b = true;
            } else {
                b = false;
            }
        } catch (Exception ex) {
            System.err.println(ex);
            b = false;
        }
        return b;
    }

    @Override
    public boolean removeSkill(int id) {
        Connection conn;
        try {
            conn = connection();

            PreparedStatement stmt = conn.prepareStatement("DELETE FROM skill WHERE id=?;");
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception ex) {
            System.err.println(ex);
            return false;
        }
    }

    @Override
    public List<Skill> getByName(String sname) {
        List<Skill> list = new ArrayList<>();
        Connection conn;
        try {
            conn = connection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM skill WHERE name LIKE ?;");
            stmt.setString(1, "%" + sname + "%");
            stmt.execute();

            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("name");
                list.add(new Skill(id, name));
            }
        } catch (Exception ex) {
            System.err.println("ERROR");
            ex.printStackTrace();
        }
        return list;
    }
}