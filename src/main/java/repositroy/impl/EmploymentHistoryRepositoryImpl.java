package repositroy.impl;

import model.EmploymentHistory;
import model.Skill;
import model.User;
import model.UserSkill;
import repositroy.inter.AbstractDao;
import repositroy.inter.EmploymentHistoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmploymentHistoryRepositoryImpl extends AbstractDao implements EmploymentHistoryRepository {

    private EmploymentHistory getEmploymentHistory(ResultSet rs) throws Exception{
        String header = rs.getString("header");
        String jobDescription = rs.getString("job_description");
        Date beginDate =rs.getDate("begin_date");
        Date endDate = rs.getDate("end_date");
        int userId = rs.getInt("user_id");
        EmploymentHistory emp =  new EmploymentHistory(null,header,beginDate,endDate,jobDescription,new User(userId));
        return  emp;
    }


    @Override
    public List<EmploymentHistory> getAllEmploymentHistoryByUserId(int userId) {
        List<EmploymentHistory> result = new ArrayList<>();

        try (Connection c = connection()) {
            PreparedStatement stmt = c.prepareStatement("select * from employment_history where user_id =? ");
            stmt.setInt(1,userId);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                EmploymentHistory emp = getEmploymentHistory(rs);
                result.add(emp);
            }
        } catch (Exception ex) {
            Logger.getLogger(UserRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
