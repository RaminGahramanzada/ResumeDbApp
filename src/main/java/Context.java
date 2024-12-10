import repositroy.impl.EmploymentHistoryRepositoryImpl;
import repositroy.impl.UserRepositoryImpl;
import repositroy.impl.UserSkillRepositoryImpl;
import repositroy.inter.EmploymentHistoryRepository;
import repositroy.inter.UserRepository;
import repositroy.inter.UserSkillRepository;

public class Context {
    public static UserRepository instanceUserRepository(){
        return new UserRepositoryImpl();
    }

    public static UserSkillRepository instanceUserSkillRepository(){
        return new UserSkillRepositoryImpl();
    }

    public static EmploymentHistoryRepository instanceEmploymentHistoryRepository(){
        return new EmploymentHistoryRepositoryImpl();
    }


}
