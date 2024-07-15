package ru.volsu.course.security.service;

import ru.volsu.course.security.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
}
