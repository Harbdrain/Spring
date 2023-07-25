package com.danil.spring.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.danil.spring.exception.FileNotFoundException;
import com.danil.spring.model.File;
import com.danil.spring.model.Status;
import com.danil.spring.repository.EventRepository;
import com.danil.spring.repository.FileRepository;
import com.danil.spring.service.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Value("${aws.bucket}")
    private String bucket;

    private final FileRepository fileRepository;
    private final EventRepository eventRepository;
    private final AmazonS3 amazonS3;

    public void uploadFile(String filename, InputStream inputStream, long contentLength) {
        long timestamp = Instant.now().getEpochSecond();
        String key = timestamp + "-" + filename;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);

        amazonS3.putObject(bucket, key, inputStream, metadata);

        File file = File.builder()
                .filename(filename)
                .location(key)
                .status(Status.ACTIVE)
                .build();
        fileRepository.save(file);
    }

    public List<File> getAllFiles() {
        List<File> files = fileRepository.findAll();
        return files;
    }

    public void downloadFile(String fileKey, Integer userId, OutputStream outputStream)
            throws IOException, FileNotFoundException {
        File file = fileRepository.findByLocation(fileKey).orElseThrow(() -> new FileNotFoundException());

        try (InputStream inputStream = amazonS3.getObject(bucket, fileKey).getObjectContent()) {
            outputStream.write(inputStream.readAllBytes());
        }
        eventRepository.saveByIds(userId, file.getId());
    }

    public void updateFile(String fileKey, InputStream inputStream, long contentLength) throws FileNotFoundException {
        fileRepository.findByLocation(fileKey).orElseThrow(() -> new FileNotFoundException());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        amazonS3.putObject(bucket, fileKey, inputStream, metadata);
    }

    public void delete(String fileKey) {
        fileRepository.deleteByLocation(fileKey);
    }
}
