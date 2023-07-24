package com.danil.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    private String username;
    private String file;

    public boolean valid() {
        return username != null && file != null && !username.isBlank() && !file.isBlank();
    }
}
