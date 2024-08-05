package dev.biddan.testingtransactional.team;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public Long save(Team newTeam) {
        return teamRepository.save(newTeam)
                .getId();
    }
}
