package ru.edjll.backend.service;

import org.springframework.stereotype.Service;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(String id) {
        return userRepository.getOne(id);
    }
}
