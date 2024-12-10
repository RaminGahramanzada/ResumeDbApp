package repositroy.inter;

import model.UserSkill;

import java.util.List;

public interface UserSkillRepository {

    public List<UserSkill> getAllSkillByUserId(int userId);
}
