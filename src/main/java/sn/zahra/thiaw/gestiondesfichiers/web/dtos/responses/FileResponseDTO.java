// FileResponseDTO.java
package sn.zahra.thiaw.gestiondesfichiers.web.dtos.responses;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileResponseDTO {

    private Long id;

    private String fileName;

    private String originalFileName;

    private String contentType;

    private Long size;

    private LocalDateTime createdAt;
}