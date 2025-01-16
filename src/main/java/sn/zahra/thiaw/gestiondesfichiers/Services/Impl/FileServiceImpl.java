// FileServiceImpl.java
package sn.zahra.thiaw.gestiondesfichiers.Services.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Repositories.FileRepository;
import sn.zahra.thiaw.gestiondesfichiers.Exceptions.ResourceNotFoundException;
import sn.zahra.thiaw.gestiondesfichiers.Services.FileService;
import sn.zahra.thiaw.gestiondesfichiers.Strategies.Which.StorageStrategyFactory;
import sn.zahra.thiaw.gestiondesfichiers.Strategies.StorageStrategy;
import sn.zahra.thiaw.gestiondesfichiers.Configs.FileStorageConfig;
import sn.zahra.thiaw.gestiondesfichiers.Validators.FileValidator;

import java.util.Optional;
import java.util.UUID;


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

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);
        String nameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        String fileName = nameWithoutExtension + UUID.randomUUID().toString() + "." + fileExtension;

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileName);
        fileEntity.setOriginalFileName(originalFileName);
        fileEntity.setContentType(file.getContentType());
        fileEntity.setSize(file.getSize());

        StorageStrategy strategy = storageStrategyFactory.getStrategy(storageType);
        strategy.store(file, fileName, fileEntity);

        return fileRepository.save(fileEntity);
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
