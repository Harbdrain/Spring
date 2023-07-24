package com.danil.spring.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danil.spring.model.File;
import com.danil.spring.service.FileService;
import com.danil.spring.service.JwtService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/files")
public class FilesController {
    private final FileService fileService;
    private final JwtService jwtService;

    @PostMapping
    @PreAuthorize("hasAuthority('file:create')")
    public ResponseEntity<Void> createFile(
            @RequestHeader("X-Filename") String filename,
            @RequestHeader("Content-Length") Long contentLength,
            InputStream inputStream) {
        if (filename == null || filename.isBlank() || contentLength == null) {
            return ResponseEntity.badRequest().build();
        }

        fileService.uploadFile(filename, inputStream, contentLength);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('file:read')")
    public ResponseEntity<List<String>> getAllFiles() {
        List<File> files = fileService.getAllFiles();
        List<String> response = new ArrayList<>();

        files.forEach(file -> response.add(file.getLocation()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{fileKey}")
    @PreAuthorize("hasAuthority('file:read')")
    public ResponseEntity<Void> getFile(
            @PathVariable String fileKey,
            @RequestHeader("Authorization") String token,
            OutputStream outputStream) {
        try {
            fileService.downloadFile(fileKey, jwtService.getUserId(token.substring(7)),
                    outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{fileKey}")
    @PreAuthorize("hasAuthority('file:update')")
    public ResponseEntity<Void> updateFile(
            @PathVariable String fileKey,
            @RequestHeader("Content-Length") Long contentLength,
            InputStream inputStream) {
        fileService.updateFile(fileKey, inputStream, contentLength);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{fileKey}")
    @PreAuthorize("hasAuthority('file:delete')")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileKey) {
        fileService.delete(fileKey);
        return ResponseEntity.ok().build();
    }
}
