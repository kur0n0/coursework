package ru.volsu.course.service;

import ru.volsu.course.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
}
