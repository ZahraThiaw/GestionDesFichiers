
// FileControllerImpl.java
package sn.zahra.thiaw.gestiondesfichiers.Web.Controllers.Impl;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.Exceptions.ResourceNotFoundException;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Mappers.FileMapper;
import sn.zahra.thiaw.gestiondesfichiers.Services.FileService;
import sn.zahra.thiaw.gestiondesfichiers.Web.Controllers.FileController;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Requests.FileRequestDTO;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.FileResponseDTO;
import sn.zahra.thiaw.gestiondesfichiers.Filters.ApiResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// FileControllerImpl.java
@RestController
@RequestMapping("/files")
public class FileControllerImpl extends BaseControllerImpl<FileEntity, Long, FileResponseDTO> implements FileController {

    private final FileService fileService;
    private final FileMapper fileMapper;

    public FileControllerImpl(FileService fileService, FileMapper fileMapper) {
        super(fileService, fileMapper);
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileResponseDTO>> uploadFile(@ModelAttribute FileRequestDTO fileRequest) {
        try {
            if (fileRequest.getFile().isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(
                        false,
                        "File is empty",
                        null,
                        List.of("File cannot be empty"),
                        "BAD_REQUEST",
                        400
                ));
            }

            FileEntity uploadedFile = fileService.uploadFile(
                    fileRequest.getFile(),
                    fileRequest.getStorageType() != null ? fileRequest.getStorageType() : StorageType.LOCAL
            );
            FileResponseDTO responseDto = fileMapper.toResponseDto(uploadedFile);

            return ResponseEntity.status(201).body(new ApiResponse<>(
                    true,
                    "File uploaded successfully",
                    responseDto,
                    null,
                    "SUCCESS",
                    201
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(
                    false,
                    "Error uploading file",
                    null,
                    List.of(e.getMessage()),
                    "ERROR",
                    500
            ));
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        try {
            // Vérifier d'abord si le fichier existe
            Optional<FileEntity> fileEntityOpt = fileService.findByIdAndDeletedFalse(id);

            if (fileEntityOpt.isEmpty()) {
                ApiResponse<Void> response = new ApiResponse<>(
                        false,
                        "File not found",
                        null,
                        List.of("No file exists with ID: " + id),
                        "NOT_FOUND",
                        404
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            FileEntity fileEntity = fileEntityOpt.get();
            byte[] data = fileService.downloadFile(id);

            // Si le fichier est trouvé mais les données sont nulles
            if (data == null || data.length == 0) {
                ApiResponse<Void> response = new ApiResponse<>(
                        false,
                        "File content not available",
                        null,
                        List.of("The file exists but its content could not be retrieved"),
                        "NOT_FOUND",
                        404
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Pour le téléchargement réussi, on renvoie le fichier avec les headers appropriés
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                    .contentType(MediaType.parseMediaType(fileEntity.getContentType()))
                    .contentLength(fileEntity.getSize())
                    .body(resource);

        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    false,
                    "File not found",
                    null,
                    List.of(e.getMessage()),
                    "NOT_FOUND",
                    404
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    false,
                    "Error downloading file",
                    null,
                    List.of(e.getMessage()),
                    "INTERNAL_SERVER_ERROR",
                    500
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FileResponseDTO>>> getAll(
            @RequestParam(value = "page", defaultValue = "1") int page, // Par défaut, page commence à 1
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "searchQuery", required = false) String searchQuery) {
        try {
            // Ajustement pour commencer les pages à 1
            if (page < 1) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(
                        false,
                        "Page number must be 1 or greater",
                        null,
                        List.of("Invalid page number"),
                        "ERROR",
                        400
                ));
            }

            Pageable pageable = PageRequest.of(page - 1, size); // Soustraire 1 pour PageRequest
            Page<FileEntity> filePage;

            if (searchQuery != null && !searchQuery.isEmpty()) {
                filePage = fileService.searchFiles(searchQuery, pageable);
                //filePage = fileService.searchActiveFiles(searchQuery, pageable);
            } else {
                filePage = fileService.getAllFiles(pageable);
                //filePage = fileService.getAllActiveFiles(pageable);
            }

            List<FileResponseDTO> dtos = filePage.getContent().stream()
                    .map(fileMapper::toResponseDto)
                    .collect(Collectors.toList());

            ApiResponse<List<FileResponseDTO>> apiResponse = new ApiResponse<>(
                    true,
                    "Files retrieved successfully",
                    dtos,
                    null,
                    "SUCCESS",
                    200
            );

            // Ajouter les métadonnées de pagination
            apiResponse.setPage(page); // Ne pas soustraire ici pour maintenir l'index utilisateur
            apiResponse.setSize(size);
            apiResponse.setTotalElements(filePage.getTotalElements());
            apiResponse.setTotalPages(filePage.getTotalPages());

            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(
                    false,
                    "Error retrieving files",
                    null,
                    List.of(e.getMessage()),
                    "ERROR",
                    500
            ));
        }
    }


    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            fileService.delete(id);
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "File deleted successfully",
                    null,
                    null,
                    "SUCCESS",
                    200
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(
                    false,
                    "Error deleting file",
                    null,
                    List.of(e.getMessage()),
                    "ERROR",
                    500
            ));
        }
    }
}