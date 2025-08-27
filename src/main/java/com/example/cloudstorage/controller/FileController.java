package com.example.cloudstorage.controller;

import com.example.cloudstorage.entity.FileEntity;
import com.example.cloudstorage.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.net.URLEncoder;

@Controller
public class FileController {

    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ✅ Download file (handles spaces & special characters)
    @GetMapping("/files/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) throws IOException {
        FileEntity fileEntity = service.getFile(id);

        if (fileEntity == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(fileEntity.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] data = Files.readAllBytes(file.toPath());

        // Encode filename to handle spaces & special characters
        String encodedFilename = URLEncoder.encode(fileEntity.getFilename(), "UTF-8").replaceAll("\\+", "%20");

        // Debug logs (optional)
        System.out.println("Downloading file: " + file.getAbsolutePath());
        System.out.println("File exists? " + file.exists());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }

    // ✅ Upload file
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            FileEntity savedFile = service.uploadFile(file);
            model.addAttribute("message", "File uploaded: " + savedFile.getFilename());
            model.addAttribute("fileId", savedFile.getId()); // for direct download link
            return "success";
        } catch (Exception e) {
            model.addAttribute("error", "Upload failed: " + e.getMessage());
            return "error";
        }
    }
}

