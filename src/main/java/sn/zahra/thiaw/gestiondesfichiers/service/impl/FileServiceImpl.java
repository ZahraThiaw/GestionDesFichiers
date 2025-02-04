// FileServiceImpl.java
package sn.zahra.thiaw.gestiondesfichiers.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.domain.entity.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.domain.enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.repository.FileRepository;
import sn.zahra.thiaw.gestiondesfichiers.exception.ResourceNotFoundException;
import sn.zahra.thiaw.gestiondesfichiers.service.FileService;
import sn.zahra.thiaw.gestiondesfichiers.service.strategy.factory.StorageStrategyFactory;
import sn.zahra.thiaw.gestiondesfichiers.service.strategy.StorageStrategy;
import sn.zahra.thiaw.gestiondesfichiers.config.FileStorageConfig;
import sn.zahra.thiaw.gestiondesfichiers.service.validator.FileValidator;

import java.util.Optional;


@Service
public class FileServiceImpl extends BaseServiceImpl<FileEntity, Long> implements FileService {

    private final FileRepository fileRepository;
    private final StorageStrategyFactory storageStrategyFactory;
    private final FileValidator fileValidator;

    public FileServiceImpl(FileRepository fileRepository,
                           StorageStrategyFactory storageStrategyFactory,
                           FileStorageConfig fileStorageConfig) {
        super(fileRepository);
        this.fileRepository = fileRepository;
        this.storageStrategyFactory = storageStrategyFactory;
        this.fileValidator = new FileValidator(fileStorageConfig);
    }

    @Override
    public FileEntity uploadFile(MultipartFile file, StorageType storageType) {
        fileValidator.validateFile(file);

        FileEntity fileEntity = new FileEntity();

        // Sélectionner la bonne stratégie et stocker le fichier
        StorageStrategy strategy = storageStrategyFactory.getStrategy(storageType);
        strategy.store(file, fileEntity);

        return fileEntity;
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    @Override
    public Optional<FileEntity> findByIdAndDeletedFalse(Long id) {
        return fileRepository.findByIdAndDeletedFalse(id);
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

        fileEntity.setDeleted(true);
        fileRepository.save(fileEntity);
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
