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
import sn.zahra.thiaw.gestiondesfichiers.Datas.Repositories.FileRepository;
import sn.zahra.thiaw.gestiondesfichiers.Exceptions.BadRequestException;
import sn.zahra.thiaw.gestiondesfichiers.Exceptions.ResourceNotFoundException;
import sn.zahra.thiaw.gestiondesfichiers.Services.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl extends BaseServiceImpl<FileEntity, Long> implements FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        super(fileRepository);
        this.fileRepository = fileRepository;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier de téléchargement");
        }
    }

    @Override
    public FileEntity uploadFile(MultipartFile file) {
        validateFile(file);

        try {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = originalFileName + "_" + UUID.randomUUID().toString();
            Path targetLocation = Paths.get(uploadDir).resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setOriginalFileName(originalFileName);
            fileEntity.setContentType(file.getContentType());
            fileEntity.setSize(file.getSize());
            fileEntity.setFilePath(targetLocation.toString());

            return fileRepository.save(fileEntity);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de stocker le fichier. Erreur: " + e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(Long id) {
        FileEntity fileEntity = fileRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));

        try {
            Path filePath = Paths.get(fileEntity.getFilePath());
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Le fichier est vide");
        }

        if (file.getSize() > 10_000_000) { // 10 MB
            throw new BadRequestException("La taille du fichier dépasse la limite maximale de 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new BadRequestException("Type de fichier non autorisé");
        }
    }

    private boolean isAllowedContentType(String contentType) {
        return contentType.startsWith("image/") ||
                contentType.equals("application/pdf") ||
                contentType.equals("application/msword") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    }

    // Méthode pour obtenir tous les fichiers avec pagination
    @Override
    public Page<FileEntity> getAllFiles(Pageable pageable) {
        return fileRepository.findAll(pageable);
    }

    // Méthode de recherche avec pagination
    @Override
    public Page<FileEntity> searchFiles(String searchQuery, Pageable pageable) {
        return fileRepository.findByFileNameContainingIgnoreCase(searchQuery, pageable);
    }
}