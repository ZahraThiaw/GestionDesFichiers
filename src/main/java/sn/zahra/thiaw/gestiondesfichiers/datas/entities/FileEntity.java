// FileEntity.java
package sn.zahra.thiaw.gestiondesfichiers.datas.entities;

import jakarta.persistence.*;
import lombok.*;
import sn.zahra.thiaw.gestiondesfichiers.datas.enums.StorageType;

@Entity
@Data
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity extends BaseEntity {

    private String fileName;

    private String originalFileName;

    private String contentType;

    private Long size;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private StorageType storageType = StorageType.LOCAL;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] fileData;
}