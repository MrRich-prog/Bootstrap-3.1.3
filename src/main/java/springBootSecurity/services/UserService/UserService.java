package springBootSecurity.services.UserService;

import springBootSecurity.models.User;

import java.util.List;

public interface UserService{

    boolean saveUser(User user);

    void removeUserById(Long id);

    List<User> getAllUsers();

    User getUserByUsername(String username);

    boolean updateUser(User user, String username, String password);

}