package repositroy.inter;

import model.User;

import java.util.List;

public interface UserRepository {
    public List<User> getAll();
    public boolean updateUser(User user);
    public boolean removeUser(int id);

    public User getById(int id);
    public boolean addUser(User user);
}
