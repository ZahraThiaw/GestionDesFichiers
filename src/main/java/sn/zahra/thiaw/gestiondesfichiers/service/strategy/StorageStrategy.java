// StorageStrategy.java
package sn.zahra.thiaw.gestiondesfichiers.service.strategy;

import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.domain.enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.domain.entity.FileEntity;


public interface StorageStrategy {
    void store(MultipartFile file, FileEntity fileEntity);
    byte[] retrieve(FileEntity fileEntity);
//    default StorageType getStorageType(){
//        return StorageType.LOCAL;
//    }
    boolean supports(StorageType storageType);
}
