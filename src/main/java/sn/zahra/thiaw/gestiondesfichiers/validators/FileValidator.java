package sn.zahra.thiaw.gestiondesfichiers.validators;

import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.exceptions.BadRequestException;
import sn.zahra.thiaw.gestiondesfichiers.configs.FileStorageConfig;

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
            //Todo toujours mettre les nombres dans des constantes avec un nom significatif
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