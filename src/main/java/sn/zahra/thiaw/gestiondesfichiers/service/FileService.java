// FileService.java
package sn.zahra.thiaw.gestiondesfichiers.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.domain.entity.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.domain.enums.StorageType;

import java.util.Optional;

public interface FileService extends BaseService<FileEntity, Long> {

    FileEntity uploadFile(MultipartFile file, StorageType storageType);

    byte[] downloadFile(Long id);

    Page<FileEntity> getAllFiles(Pageable pageable);

    Page<FileEntity> searchFiles(String searchQuery, Pageable pageable);

    Optional<FileEntity> findByIdAndDeletedFalse(Long id);
}