package ru.edjll.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
