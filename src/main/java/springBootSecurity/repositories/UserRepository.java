package springBootSecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springBootSecurity.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}