package dev.biddan.sessionloginusingcookie;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginSessionRepository extends JpaRepository<LoginSession, Long> {

    Optional<LoginSession> findByAccessId(String sessionId);
}
