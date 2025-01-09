package sn.zahra.thiaw.gestiondesfichiers.Validators;

import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Exceptions.BadRequestException;
import sn.zahra.thiaw.gestiondesfichiers.Configs.FileStorageConfig;

public class FileValidator {
    private final FileStorageConfig fileStorageConfig;

    public FileValidator(FileStorageConfig fileStorageConfig) {
        this.fileStorageConfig = fileStorageConfig;
    }

    public void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (file.getSize() > fileStorageConfig.getMaxFileSize()) {
            throw new BadRequestException("File size exceeds maximum limit of " +
                    fileStorageConfig.getMaxFileSize() / 1_000_000 + "MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new BadRequestException("File type not allowed. Allowed types: " +
                    String.join(", ", fileStorageConfig.getAllowedContentTypes()));
        }
    }

    private boolean isAllowedContentType(String contentType) {
        return fileStorageConfig.getAllowedContentTypes().contains(contentType);
    }
}