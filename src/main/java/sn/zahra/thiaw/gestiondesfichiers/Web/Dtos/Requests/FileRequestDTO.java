// FileRequestDTO.java
package sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileRequestDTO {

    private MultipartFile file;
}