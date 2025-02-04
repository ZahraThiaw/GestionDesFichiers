package sn.zahra.thiaw.gestiondesfichiers.service.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.exception.BadRequestException;
import sn.zahra.thiaw.gestiondesfichiers.config.FileStorageConfig;

public class FileValidator {

    @Value("${file.upload.max-file-size}")
    private long maxSizefile ;

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
                    fileStorageConfig.getMaxFileSize() / maxSizefile + "MB");
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