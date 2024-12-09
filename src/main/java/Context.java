import repositroy.impl.UserRepositoryImpl;
import repositroy.inter.UserRepository;

public class Context {
    public static UserRepository instanceUserRepository(){
        return new UserRepositoryImpl();
    }
}
