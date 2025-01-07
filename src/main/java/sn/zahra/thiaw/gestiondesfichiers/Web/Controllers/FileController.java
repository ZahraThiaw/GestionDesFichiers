// FileController.java
package sn.zahra.thiaw.gestiondesfichiers.Web.Controllers;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.FileResponseDTO;
import sn.zahra.thiaw.gestiondesfichiers.Web.Filters.ApiResponse;

public interface FileController extends BaseController<FileEntity, Long, FileResponseDTO> {

    ResponseEntity<ApiResponse<FileResponseDTO>> uploadFile(MultipartFile file);
    ResponseEntity<ByteArrayResource> downloadFile(Long id);
}