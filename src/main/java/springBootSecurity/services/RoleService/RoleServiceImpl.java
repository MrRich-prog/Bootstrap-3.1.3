package springBootSecurity.services.RoleService;

import org.springframework.stereotype.Service;
import springBootSecurity.models.Role;
import springBootSecurity.repositories.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Set<Role> getRoleByName(List<String> roles) {

        Set<Role> rolesH = new HashSet<>();
        for (String role : roles) {
            rolesH.add(roleRepository.findByName(role));
        }
        return rolesH;
    }

    @Override
    public Set<Role> getRoleById(Long id) {
        Set<Role> roles = new HashSet<>();
        Optional<Role> role = roleRepository.findById(id);
        role.ifPresent(roles::add);
        return roles;
    }
}
