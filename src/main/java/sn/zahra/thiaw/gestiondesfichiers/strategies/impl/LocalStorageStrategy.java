package sn.zahra.thiaw.gestiondesfichiers.strategies.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.datas.entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.datas.enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.datas.repositories.FileRepository;
import sn.zahra.thiaw.gestiondesfichiers.strategies.StorageStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class LocalStorageStrategy implements StorageStrategy {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private final FileRepository fileRepository;

    public LocalStorageStrategy(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostConstruct
    public void initialize() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @Override
    public void store(MultipartFile file, String fileName, FileEntity fileEntity) {
        try {
            Path targetLocation = Paths.get(uploadDir).resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            fileEntity.setFilePath(fileName);
            fileEntity.setStorageType(StorageType.LOCAL);
            fileRepository.save(fileEntity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file locally", e);
        }
    }

    @Override
    public byte[] retrieve(FileEntity fileEntity) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileEntity.getFilePath());
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    @Override
    public boolean supports(StorageType storageType) {
        return StorageType.LOCAL.equals(storageType);
    }
}