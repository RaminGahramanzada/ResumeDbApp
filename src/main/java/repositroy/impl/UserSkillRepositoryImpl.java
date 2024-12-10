package repositroy.impl;

import model.Skill;
import model.User;
import model.UserSkill;
import repositroy.inter.AbstractDao;
import repositroy.inter.UserSkillRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserSkillRepositoryImpl extends AbstractDao implements UserSkillRepository {

    private UserSkill getUserSkill(ResultSet rs) throws Exception{
        int userId = rs.getInt("id");
        int skillId = rs.getInt("skill_id");

        String skillName = rs.getString("skill_name");
        int power = rs.getInt("power");

        return new UserSkill(null,new User(userId),new Skill(skillId,skillName),power);
    }


    @Override
    public List<UserSkill> getAllSkillByUserId(int userId) {
        List<UserSkill> result = new ArrayList<>();

        try (Connection c = connection()) {
            PreparedStatement stmt = c.prepareStatement("SELECT "
                    + "  u.*, "
                    + "  us.skill_id, "
                    + "  s.NAME AS skill_name, "
                    + "  us.power "
                    + "FROM "
                    + "  user_skill us "
                    + "  LEFT JOIN USER u ON us.user_id = u.id "
                    + "  LEFT JOIN skill s ON us.skill_id = s.id "
                    + "WHERE "
                    + "  us.user_id = ?");
            stmt.setInt(1,userId);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                UserSkill u = getUserSkill(rs);
                result.add(u);
            }
        } catch (Exception ex) {
            Logger.getLogger(UserRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
