// FileServiceImpl.java
package sn.zahra.thiaw.gestiondesfichiers.Services.Impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Repositories.FileRepository;
import sn.zahra.thiaw.gestiondesfichiers.Exceptions.BadRequestException;
import sn.zahra.thiaw.gestiondesfichiers.Exceptions.ResourceNotFoundException;
import sn.zahra.thiaw.gestiondesfichiers.Services.FileService;
import sn.zahra.thiaw.gestiondesfichiers.Services.Storage.Impl.StorageStrategyFactory;
import sn.zahra.thiaw.gestiondesfichiers.Services.Storage.StorageStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

// FileServiceImpl.java
@Service
public class FileServiceImpl extends BaseServiceImpl<FileEntity, Long> implements FileService {

    private final FileRepository fileRepository;
    private final StorageStrategyFactory storageStrategyFactory;

    public FileServiceImpl(FileRepository fileRepository, StorageStrategyFactory storageStrategyFactory) {
        super(fileRepository);
        this.fileRepository = fileRepository;
        this.storageStrategyFactory = storageStrategyFactory;
    }

    @Override
    public FileEntity uploadFile(MultipartFile file, StorageType storageType) {
        validateFile(file);

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = originalFileName + "_" + UUID.randomUUID().toString();

        StorageStrategy strategy = storageStrategyFactory.getStrategy(storageType);
        FileEntity fileEntity = strategy.store(file, fileName);

        return fileRepository.save(fileEntity);
    }

    @Override
    public byte[] downloadFile(Long id) {
        FileEntity fileEntity = fileRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));

        StorageStrategy strategy = storageStrategyFactory.getStrategy(fileEntity.getStorageType());
        return strategy.retrieve(fileEntity);
    }

    @Override
    public void delete(Long id) {
        FileEntity fileEntity = getById(id);
        StorageStrategy strategy = storageStrategyFactory.getStrategy(fileEntity.getStorageType());
        strategy.delete(fileEntity);

        fileEntity.setDeleted(true);
        fileRepository.save(fileEntity);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (file.getSize() > 10_000_000) { // 10 MB
            throw new BadRequestException("File size exceeds maximum limit of 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new BadRequestException("File type not allowed");
        }
    }

    private boolean isAllowedContentType(String contentType) {
        return contentType.startsWith("image/") ||
                contentType.equals("application/pdf") ||
                contentType.equals("application/msword") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    }

    @Override
    public Page<FileEntity> getAllFiles(Pageable pageable) {
        return fileRepository.findAll(pageable);
    }

    @Override
    public Page<FileEntity> searchFiles(String searchQuery, Pageable pageable) {
        return fileRepository.findByFileNameContainingIgnoreCase(searchQuery, pageable);
    }
}
