package dev.biddan.testingtransactional.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저를 등록한다.")
    @Test
    void registerUser_success() {
        // given
        var newUser = User.builder()
                .name("원래 이름")
                .build();

        // when
        Long newUserId = userService.registerUser(newUser);

        // then
        assertThat(newUserId).isNotNull();
        assertThat(userRepository.findById(newUserId)).isPresent();
    }

    @DisplayName("유저가 존재하는 경우, 유저의 이름을 변경한다.")
    @Test
    void changeName_success() {
        // given
        // 유저를 생성한다.
        var user = User.builder()
                .name("원래 이름")
                .build();
        var userId = userService.registerUser(user);

        // when
        // 유저의 이름을 변경한다.
        var newName = "변경 이름";
        userService.changeName(userId, newName);

        var foundUser = userService.findUser(userId);

        // then
        // 유저의 이름이 변경되어야 한다.
        assertThat(foundUser.getName()).isEqualTo("변경 이름");
    }
}
