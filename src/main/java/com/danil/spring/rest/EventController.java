package com.danil.spring.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danil.spring.dto.CreateEventRequest;
import com.danil.spring.dto.GetAllEventsResponse;
import com.danil.spring.dto.UpdateEventRequest;
import com.danil.spring.model.Event;
import com.danil.spring.service.EventService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasAuthority('event:create')")
    public ResponseEntity<Void> createEvent(@RequestBody CreateEventRequest createEventRequest) {
        if (createEventRequest == null || !createEventRequest.valid()) {
            return ResponseEntity.badRequest().build();
        }

        eventService.createEvent(createEventRequest.getUserId(), createEventRequest.getFileId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('event:read')")
    public ResponseEntity<List<GetAllEventsResponse>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        List<GetAllEventsResponse> response = new ArrayList<>();
        events.forEach(event -> response.add(
                GetAllEventsResponse.builder()
                        .eventId(event.getId())
                        .username(event.getUser().getUsername())
                        .file(event.getFile().getLocation()).build()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('event:update')")
    public ResponseEntity<Void> updateEvent(@PathVariable Integer id,
            @RequestBody UpdateEventRequest updateEventRequest) {
        if (updateEventRequest == null || !updateEventRequest.valid()) {
            return ResponseEntity.badRequest().build();
        }

        eventService.updateEvent(id, updateEventRequest.getUsername(), updateEventRequest.getFile());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('event:delete')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
}
