import model.User;
import repositroy.impl.UserRepositoryImpl;
import repositroy.inter.EmploymentHistoryRepository;
import repositroy.inter.UserRepository;
import repositroy.inter.UserSkillRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {



    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        EmploymentHistoryRepository employmentHistoryRepository = Context.instanceEmploymentHistoryRepository();


        System.out.println(employmentHistoryRepository.getAllEmploymentHistoryByUserId(5));


    }
}
