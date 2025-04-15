package springBootSecurity.services.RoleService;

import springBootSecurity.models.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getAllRoles();
    Set<Role> getRoleByName(List<String> roles);
    Set<Role> getRoleById(Long id);
}
