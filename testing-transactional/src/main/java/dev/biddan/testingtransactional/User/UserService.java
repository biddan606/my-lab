package dev.biddan.testingtransactional.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long registerUser(User newUser) {
        var savedUser = userRepository.save(newUser);
        return savedUser.getId();
    }

    @Transactional(readOnly = true)
    public User findUser(long userId) {
        var foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾지 못헀습니다."));

        return foundUser;
    }

    @Transactional
    public void changeName(Long userId, String newName) {
        userRepository.findById(userId)
                .ifPresent(user -> user.changeName(newName));
    }
}
