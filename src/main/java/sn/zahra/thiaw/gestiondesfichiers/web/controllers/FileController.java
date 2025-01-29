// FileController.java
package sn.zahra.thiaw.gestiondesfichiers.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import sn.zahra.thiaw.gestiondesfichiers.datas.entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.web.dtos.requests.FileRequestDTO;
import sn.zahra.thiaw.gestiondesfichiers.web.dtos.responses.FileResponseDTO;
import sn.zahra.thiaw.gestiondesfichiers.filters.ApiResponse;

public interface FileController extends BaseController<FileEntity, Long, FileResponseDTO> {

    ResponseEntity<ApiResponse<FileResponseDTO>> uploadFile(@ModelAttribute FileRequestDTO fileRequest);

    ResponseEntity<?> downloadFile(Long id);
}