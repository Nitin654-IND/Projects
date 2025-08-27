package com.example.cloudstorage.service;

import com.example.cloudstorage.entity.FileEntity;
import com.example.cloudstorage.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FileRepository repository;

    public FileService(FileRepository repository) {
        this.repository = repository;
    }

    // ✅ Upload file
    public FileEntity uploadFile(MultipartFile file) throws IOException {
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs(); // create folder if not exists
        }

        String filePath = uploadDir + "/" + file.getOriginalFilename();

        // Save file to local folder
        file.transferTo(new File(filePath));

        // Save only metadata in DB
        FileEntity fileEntity = new FileEntity(file.getOriginalFilename(), filePath);
        return repository.save(fileEntity);
    }

    public FileEntity getFile(Long id) {
        return repository.findById(id).orElse(null);
    }

}

