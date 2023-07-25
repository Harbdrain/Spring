package com.danil.spring.service;

import java.util.List;

import com.danil.spring.model.Event;

public interface EventService {
    void createEvent(Integer userId, Integer fileId);

    List<Event> getAllEvents();

    void updateEvent(Integer id, String username, String filename);

    void deleteEvent(Integer id);
}
