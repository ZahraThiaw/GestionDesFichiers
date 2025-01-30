// DatabaseStorageStrategy.java
package sn.zahra.thiaw.gestiondesfichiers.strategies.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.datas.entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.datas.enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.datas.repositories.FileRepository;
import sn.zahra.thiaw.gestiondesfichiers.strategies.StorageStrategy;

import java.io.IOException;
import java.util.UUID;

@Service
public class DatabaseStorageStrategy implements StorageStrategy {

    private final FileRepository fileRepository;

    public DatabaseStorageStrategy(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

//    @Override
//    public void store(MultipartFile file, String fileName, FileEntity fileEntity) {
//        try {
//            fileEntity.setFileData(file.getBytes());
//            fileEntity.setStorageType(StorageType.DATABASE);
//            fileRepository.save(fileEntity);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to store file in database", e);
//        }
//    }


    @Override
    public void store(MultipartFile file, FileEntity fileEntity) {
        try {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = getFileExtension(originalFileName);
            String nameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
            String fileName = nameWithoutExtension + "_" + UUID.randomUUID().toString() + "." + fileExtension;

            fileEntity.setFileData(file.getBytes());
            fileEntity.setStorageType(StorageType.DATABASE);
            fileEntity.setFileName(fileName);
            fileEntity.setOriginalFileName(originalFileName);
            fileEntity.setContentType(file.getContentType());
            fileEntity.setSize(file.getSize());

            fileRepository.save(fileEntity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file in database", e);
        }
    }

    @Override
    public byte[] retrieve(FileEntity fileEntity) {
        return fileEntity.getFileData();
    }

    //todo embarquer le storage type

    @Override
    public boolean supports(StorageType storageType) {
        return StorageType.DATABASE.equals(storageType);
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
