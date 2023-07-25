package com.danil.spring.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.danil.spring.exception.UserNotFoundException;
import com.danil.spring.model.Event;
import com.danil.spring.model.Status;
import com.danil.spring.model.User;
import com.danil.spring.model.UserRole;
import com.danil.spring.repository.UserRepository;
import com.danil.spring.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
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
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            user = null;
        }

        return Optional.ofNullable(user);
    }

    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User findUser(String username) {
        User user = userRepository.findByUsernameFull(username).orElseThrow(() -> new UserNotFoundException());
        if (user.getStatus() == Status.DELETED) {
            throw new UserNotFoundException();
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

        return user;
    }

    public void updateUser(String username, String newUsername, String password, UserRole role) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());

        user.setUsername(newUsername);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user = userRepository.save(user);
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
}
