package dev.biddan.sessionloginusingcookie;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginSessionRepository loginSessionRepository;

    @DisplayName("유저 아이디와 비밀번호가 매칭된다면 새로운 세션 아이디를 발급한다.")
    @Test
    void createSession_success() {
        // given
        User user = User.builder()
                .loginId("loginId")
                .password("password")
                .build();
        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .id(user.getLoginId())
                .password(user.getPassword())
                .build();

        // when
        LoginSession actual = authenticationService.createSession(request);

        // then
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getUser().getId()).isEqualTo(user.getId());
        Assertions.assertThat(actual.getAccessId()).isNotNull();
    }

    @DisplayName("유효한 세션 아이디로 검증 시 예외가 발생하지 않는다.")
    @Test
    void validateSession_success() {
        // given
        LoginSession loginSession = LoginSession.builder()
                .accessId(UUID.randomUUID().toString())
                .build();

        loginSessionRepository.save(loginSession);

        // when & then
        Assertions.assertThatCode(() ->
                authenticationService.validateSession(loginSession.getAccessId())
        ).doesNotThrowAnyException();
    }
}
