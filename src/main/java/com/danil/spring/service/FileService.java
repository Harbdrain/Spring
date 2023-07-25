package com.danil.spring.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.danil.spring.model.File;

public interface FileService {
    void uploadFile(String filename, InputStream inputStream, long contentLength);

    List<File> getAllFiles();

    void downloadFile(String fileKey, Integer userId, OutputStream outputStream) throws IOException;

    void updateFile(String fileKey, InputStream inputStream, long contentLength);

    void delete(String fileKey);
}
