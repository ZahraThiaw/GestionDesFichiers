// FileController.java
package sn.zahra.thiaw.gestiondesfichiers.Web.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Requests.FileRequestDTO;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.FileResponseDTO;
import sn.zahra.thiaw.gestiondesfichiers.Filters.ApiResponse;

public interface FileController extends BaseController<FileEntity, Long, FileResponseDTO> {

    ResponseEntity<ApiResponse<FileResponseDTO>> uploadFile(@ModelAttribute FileRequestDTO fileRequest);

    ResponseEntity<?> downloadFile(Long id);
}