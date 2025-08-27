package com.example.cloudstorage.entity;

import jakarta.persistence.*;

@Entity
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String filePath;   // where file is saved on disk

    public FileEntity() {}

    public FileEntity(String filename, String filePath) {
        this.filename = filename;
        this.filePath = filePath;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}




