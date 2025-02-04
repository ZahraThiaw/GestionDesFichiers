package sn.zahra.thiaw.gestiondesfichiers.service.strategy.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.domain.entity.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.domain.enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.repository.FileRepository;
import sn.zahra.thiaw.gestiondesfichiers.service.strategy.StorageStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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
    public void store(MultipartFile file, FileEntity fileEntity) {
        try {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = getFileExtension(originalFileName);
            String nameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
            String fileName = nameWithoutExtension + "_" + UUID.randomUUID().toString() + "." + fileExtension;

            Path targetLocation = Paths.get(uploadDir).resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            fileEntity.setFilePath(fileName);
            fileEntity.setStorageType(StorageType.LOCAL);
            fileEntity.setFileName(fileName);
            fileEntity.setOriginalFileName(originalFileName);
            fileEntity.setContentType(file.getContentType());
            fileEntity.setSize(file.getSize());

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

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}