// FileService.java
package sn.zahra.thiaw.gestiondesfichiers.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;

public interface FileService extends BaseService<FileEntity, Long> {
    FileEntity uploadFile(MultipartFile file);
    byte[] downloadFile(Long id);
    Page<FileEntity> getAllFiles(Pageable pageable);
    Page<FileEntity> searchFiles(String searchQuery, Pageable pageable);
}
