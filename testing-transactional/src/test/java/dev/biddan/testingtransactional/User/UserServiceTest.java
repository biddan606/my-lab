package dev.biddan.testingtransactional.User;

import static org.assertj.core.api.Assertions.assertThat;

import dev.biddan.testingtransactional.team.Team;
import dev.biddan.testingtransactional.team.TeamService;
import dev.biddan.testingtransactional.util.DatabaseCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private TeamService teamService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void destroy() {
        databaseCleaner.cleanDatabase();
    }

    @DisplayName("유저를 등록한다.")
    @Test
    void save_success() {
        // given
        var newUser = User.builder()
                .name("원래 이름")
                .build();

        // when
        var newUserId = userService.save(newUser);

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
        var userId = userService.save(user);

        // when
        // 유저의 이름을 변경한다.
        var newName = "변경 이름";
        userService.changeName(userId, newName);

        var foundUser = userService.get(userId);

        // then
        // 유저의 이름이 변경되어야 한다.
        assertThat(foundUser.getName()).isEqualTo("변경 이름");
    }

    @DisplayName("유저에 팀을 할당합니다.")
    @Test
    void assignTeam_success() {
        // given
        // 유저를 생성한다.
        var newUser = User.builder()
                .name("유저 이름")
                .build();
        var userId = userService.save(newUser);

        // 팀을 생성한다.
        var newTeam = Team.builder()
                .name("팀 이름")
                .build();
        var newTeamId = teamService.save(newTeam);

        // when
        userService.assignTeam(userId, newTeamId);

        // then
        var user = userService.getWithTeam(userId);

        assertThat(user).isNotNull();
        assertThat(user.getTeam()).isNotNull();
        assertThat(user.getTeam().getId()).isEqualTo(newTeam.getId());
    }
}
