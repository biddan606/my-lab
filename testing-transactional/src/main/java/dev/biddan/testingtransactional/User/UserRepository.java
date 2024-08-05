package dev.biddan.testingtransactional.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    default User getUserById(Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
    }

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.team")
    Optional<User> findUserWithTeamById(Long id);
}
