// StorageStrategy.java
package sn.zahra.thiaw.gestiondesfichiers.Services.Storage;

import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;

public interface StorageStrategy {
    FileEntity store(MultipartFile file, String fileName);
    byte[] retrieve(FileEntity fileEntity);
    void delete(FileEntity fileEntity);
    boolean supports(StorageType storageType);
}