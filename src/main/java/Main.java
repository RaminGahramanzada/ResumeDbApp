import model.User;
import repositroy.impl.UserRepositoryImpl;
import repositroy.inter.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {



    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserRepository userRepository = new UserRepositoryImpl();


        System.out.println(userRepository.getAllSkillByUserId(5));


    }
}
