// LocalStorageStrategy.java
package sn.zahra.thiaw.gestiondesfichiers.Services.Storage.Impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.Services.Storage.StorageStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class LocalStorageStrategy implements StorageStrategy {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void initialize() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @Override
    public FileEntity store(MultipartFile file, String fileName) {
        try {
            Path targetLocation = Paths.get(uploadDir).resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setOriginalFileName(file.getOriginalFilename());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setSize(file.getSize());
            fileEntity.setFilePath(targetLocation.toString());
            fileEntity.setStorageType(StorageType.LOCAL);

            return fileEntity;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file locally", e);
        }
    }

    @Override
    public byte[] retrieve(FileEntity fileEntity) {
        try {
            Path filePath = Paths.get(fileEntity.getFilePath());
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    @Override
    public void delete(FileEntity fileEntity) {
        try {
            Files.deleteIfExists(Paths.get(fileEntity.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    @Override
    public boolean supports(StorageType storageType) {
        return StorageType.LOCAL.equals(storageType);
    }
}