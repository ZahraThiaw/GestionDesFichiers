// FileEntity.java
package sn.zahra.thiaw.gestiondesfichiers.Datas.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

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
}