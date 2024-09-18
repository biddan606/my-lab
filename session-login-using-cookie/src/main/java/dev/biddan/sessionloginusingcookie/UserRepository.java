package dev.biddan.sessionloginusingcookie;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginIdAndPassword(String id, String password);
}
