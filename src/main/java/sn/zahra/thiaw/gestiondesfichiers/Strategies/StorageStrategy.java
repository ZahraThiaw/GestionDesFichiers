// StorageStrategy.java
package sn.zahra.thiaw.gestiondesfichiers.Strategies;

import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;


public interface StorageStrategy {
    void store(MultipartFile file, String fileName, FileEntity fileEntity);
    byte[] retrieve(FileEntity fileEntity);
    void delete(FileEntity fileEntity);
    boolean supports(StorageType storageType);
}