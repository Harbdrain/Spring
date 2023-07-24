package com.danil.spring.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.danil.spring.exception.BadRequestException;
import com.danil.spring.exception.EventNotFoundException;
import com.danil.spring.model.Event;
import com.danil.spring.model.File;
import com.danil.spring.model.User;
import com.danil.spring.repository.EventRepository;
import com.danil.spring.repository.FileRepository;
import com.danil.spring.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    public void createEvent(Integer userId, Integer fileId) {
        userRepository.findById(userId).orElseThrow(() -> new BadRequestException());
        fileRepository.findById(fileId).orElseThrow(() -> new BadRequestException());

        eventRepository.saveByIds(userId, fileId);
    }

    public List<Event> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events;
    }

    public void updateEvent(Integer id, String username, String filename) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException());
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BadRequestException());
        File file = fileRepository.findByLocation(filename).orElseThrow(() -> new BadRequestException());
        event.setUser(user);
        event.setFile(file);

        eventRepository.save(event);
    }

    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }
}
