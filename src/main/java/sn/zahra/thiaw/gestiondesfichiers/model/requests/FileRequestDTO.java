// FileRequestDTO.java
package sn.zahra.thiaw.gestiondesfichiers.model.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.domain.enums.StorageType;

@Data
public class FileRequestDTO {
//Todo mettre des contraintes de validation

    @NotNull
    @Schema(description = "Le fichier à upload avec extension")
    private MultipartFile file;


    //Todo documentation Open API pour le swagger
    @Schema(description = "Choisir le type de stockage entre local et database")
    private StorageType storageType = StorageType.LOCAL;
}