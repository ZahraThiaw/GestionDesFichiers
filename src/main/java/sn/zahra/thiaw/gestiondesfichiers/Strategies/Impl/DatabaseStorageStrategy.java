// DatabaseStorageStrategy.java
package sn.zahra.thiaw.gestiondesfichiers.Strategies.Impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.Strategies.StorageStrategy;

import java.io.IOException;

@Service
public class DatabaseStorageStrategy implements StorageStrategy {
    @Override
    public void store(MultipartFile file, String fileName, FileEntity fileEntity) {
        try {
            fileEntity.setFileData(file.getBytes());
            fileEntity.setStorageType(StorageType.DATABASE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file in database", e);
        }
    }

    @Override
    public byte[] retrieve(FileEntity fileEntity) {
        return fileEntity.getFileData();
    }

    @Override
    public void delete(FileEntity fileEntity) {
        // Nothing to do as file data will be deleted with the entity
    }

    @Override
    public boolean supports(StorageType storageType) {
        return StorageType.DATABASE.equals(storageType);
    }
}
