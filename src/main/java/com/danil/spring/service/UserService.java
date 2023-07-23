package com.danil.spring.service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.danil.spring.model.Event;
import com.danil.spring.model.Status;
import com.danil.spring.model.User;
import com.danil.spring.model.UserRole;
import com.danil.spring.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String username, String password, UserRole role) {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .status(Status.ACTIVE)
                .build();
        return userRepository.save(user);
    }

    public User createUser(String username, String password) {
        return createUser(username, password, UserRole.USER);
    }

    public Optional<User> validateUser(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())
                || user.getStatus() == Status.DELETED) {
            user = null;
        }

        return Optional.ofNullable(user);
    }

    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getStatus() == Status.DELETED) {
                iterator.remove();
            }
        }

        return users;
    }

    public Optional<User> findUser(String username) {
        User user = userRepository.findByUsernameFull(username).orElse(null);
        if (user == null || user.getStatus() == Status.DELETED) {
            return Optional.ofNullable(null);
        }

        Iterator<Event> eventIterator = user.getEvents().iterator();
        while (eventIterator.hasNext()) {
            Event event = eventIterator.next();
            if (event.getStatus() == Status.DELETED) {
                eventIterator.remove();
                continue;
            }

            if (event.getFile().getStatus() == Status.DELETED) {
                eventIterator.remove();
                continue;
            }
        }

        return Optional.of(user);
    }

    public Optional<User> updateUser(String username, String newUsername, String password, UserRole role) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return Optional.ofNullable(null);
        }

        user.setUsername(newUsername);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user = userRepository.save(user);
        return Optional.of(user);
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
}
