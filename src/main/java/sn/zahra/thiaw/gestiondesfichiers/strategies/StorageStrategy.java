// StorageStrategy.java
package sn.zahra.thiaw.gestiondesfichiers.strategies;

import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.datas.enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.datas.entities.FileEntity;


public interface StorageStrategy {
    void store(MultipartFile file, FileEntity fileEntity);
    byte[] retrieve(FileEntity fileEntity);
//    default StorageType getStorageType(){
//        return StorageType.LOCAL;
//    }
    boolean supports(StorageType storageType);
}
