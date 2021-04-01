package ru.edjll.backend.service;

import org.springframework.stereotype.Service;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
}
