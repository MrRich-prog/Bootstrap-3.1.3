package springBootSecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springBootSecurity.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findById(long id);

    Role findByName(String name);
}
