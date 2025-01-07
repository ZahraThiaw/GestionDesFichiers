// FileService.java
package sn.zahra.thiaw.gestiondesfichiers.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;

public interface FileService extends BaseService<FileEntity, Long> {
    //FileEntity uploadFile(MultipartFile file);
    FileEntity uploadFile(MultipartFile file, StorageType storageType);
    byte[] downloadFile(Long id);
    Page<FileEntity> getAllFiles(Pageable pageable);
    Page<FileEntity> searchFiles(String searchQuery, Pageable pageable);
}
