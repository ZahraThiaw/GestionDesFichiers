// DatabaseStorageStrategy.java
package sn.zahra.thiaw.gestiondesfichiers.Services.Storage.Impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.Services.Storage.StorageStrategy;

import java.io.IOException;

@Service
public class DatabaseStorageStrategy implements StorageStrategy {

    @Override
    public FileEntity store(MultipartFile file, String fileName) {
        try {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setOriginalFileName(file.getOriginalFilename());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setSize(file.getSize());
            fileEntity.setFileData(file.getBytes());
            fileEntity.setStorageType(StorageType.DATABASE);

            return fileEntity;
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
