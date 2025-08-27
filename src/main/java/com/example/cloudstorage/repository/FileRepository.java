package com.example.cloudstorage.repository;

import com.example.cloudstorage.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
