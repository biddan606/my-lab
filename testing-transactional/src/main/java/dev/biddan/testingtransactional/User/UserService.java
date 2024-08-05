package dev.biddan.testingtransactional.User;

import dev.biddan.testingtransactional.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public Long save(User newUser) {
        return userRepository.save(newUser)
                .getId();
    }

    @Transactional(readOnly = true)
    public User get(long userId) {
        return userRepository.getUserById(userId);

    }

    @Transactional
    public void changeName(Long userId, String newName) {
        userRepository.findById(userId)
                .ifPresent(user -> user.changeName(newName));
    }

    @Transactional
    public void assignTeam(Long userId, Long teamIdToAssign) {
        var user = userRepository.getUserById(userId);
        var teamToAssign = teamRepository.findTeamWithUsersById(teamIdToAssign)
                .orElseThrow(() -> new IllegalArgumentException("배정할 팀이 존재하지 않습니다."));

        teamToAssign.addUser(user);
    }

    @Transactional(readOnly = true)
    public User getWithTeam(Long userId) {
        return userRepository.findUserWithTeamById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
    }
}
