// FileRequestDTO.java
package sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;

@Data
public class FileRequestDTO {

    private MultipartFile file;
    private StorageType storageType = StorageType.LOCAL;
}