package dev.biddan.sessionloginusingcookie;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final SessionIdGenerator sessionIdGenerator;
    private final LoginSessionRepository loginSessionRepository;

    @Transactional
    public LoginSession createSession(LoginRequest request) {
        User foundUser = userRepository.findByLoginIdAndPassword(request.id(), request.password())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String accessId = sessionIdGenerator.generate();
        LoginSession loginSession = LoginSession.builder()
                .accessId(accessId)
                .user(foundUser)
                .build();

        return loginSessionRepository.save(loginSession);
    }

    public void validateSession(String sessionId) {
        loginSessionRepository.findByAccessId(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
