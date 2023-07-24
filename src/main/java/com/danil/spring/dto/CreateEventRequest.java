package com.danil.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {
    Integer userId;
    Integer fileId;

    public boolean valid() {
        return userId != null && fileId != null;
    }
}
