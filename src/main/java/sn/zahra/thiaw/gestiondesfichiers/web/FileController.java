// FileController.java
package sn.zahra.thiaw.gestiondesfichiers.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import sn.zahra.thiaw.gestiondesfichiers.domain.entity.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.model.requests.FileRequestDTO;
import sn.zahra.thiaw.gestiondesfichiers.model.responses.FileResponseDTO;
import sn.zahra.thiaw.gestiondesfichiers.config.filter.ApiResponse;

public interface FileController extends BaseController<FileEntity, Long, FileResponseDTO> {

    ResponseEntity<ApiResponse<FileResponseDTO>> uploadFile(@ModelAttribute FileRequestDTO fileRequest);

    ResponseEntity<?> downloadFile(Long id);
}