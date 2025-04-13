package springBootSecurity.services;

import springBootSecurity.models.Role;
import springBootSecurity.models.User;

import java.util.List;
import java.util.Set;

public interface UserService{

    boolean saveUser(User user);

    void removeUserById(Long id);

    List<User> getAllUsers();

    void cleanUsersTable();

    User getUserById(Long id);

    User getUserByUsername(String username);

    boolean updateUser(User user);

    Set<Role> getRoles(String roleName);
}