package dev.biddan.testingtransactional.team;

import static org.assertj.core.api.Assertions.assertThat;

import dev.biddan.testingtransactional.util.DatabaseCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void destroy() {
        databaseCleaner.cleanDatabase();
    }

    @DisplayName("팀을 등록한다.")
    @Test
    void save_success() {
        // given
        var newTeam = Team.builder()
                .name("원래 이름")
                .build();

        // when
        var newTeamId = teamService.save(newTeam);

        // then
        assertThat(newTeamId).isNotNull();
        assertThat(teamRepository.findById(newTeamId)).isPresent();
    }
}
