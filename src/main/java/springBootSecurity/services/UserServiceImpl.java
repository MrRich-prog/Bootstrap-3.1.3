package springBootSecurity.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springBootSecurity.repositories.RoleRepository;
import springBootSecurity.repositories.UserRepository;
import springBootSecurity.models.Role;
import springBootSecurity.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public boolean saveUser(User user) {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Transactional
    @Override
    public void removeUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public boolean updateUser(User user, String usernameOld, String passwordOld) {

        if (userRepository.findByUsername(user.getUsername()) != null) {
            if (!usernameOld.equals(user.getUsername())) {
                return false;
            }
        }
        if(!passwordOld.equals(user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        return true;
    }

    public Set<Role> getRoles(String roleName) {
        Set<Role> roles = new HashSet<>();
        switch (roleName){
            case "ROLE_ADMIN":
                roles.add(roleRepository.findById(1L));
                break;
            case "ROLE_USER":
                roles.add(roleRepository.findById(2L));
                break;
            case "ROLE_ADMIN,ROLE_USER":
                roles.add(roleRepository.findById(1L));
                roles.add(roleRepository.findById(2L));
                break;
        }
        return roles;
    }
}