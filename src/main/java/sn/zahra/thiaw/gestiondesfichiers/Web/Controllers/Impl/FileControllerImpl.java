
// FileControllerImpl.java
package sn.zahra.thiaw.gestiondesfichiers.Web.Controllers.Impl;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Mappers.FileMapper;
import sn.zahra.thiaw.gestiondesfichiers.Services.FileService;
import sn.zahra.thiaw.gestiondesfichiers.Web.Controllers.FileController;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.FileResponseDTO;
import sn.zahra.thiaw.gestiondesfichiers.Web.Filters.ApiResponse;

import java.util.List;
import java.util.stream.Collectors;

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
    @Override
    public ResponseEntity<ApiResponse<FileResponseDTO>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(
                        false,
                        "Le fichier est vide",
                        null,
                        List.of("Le fichier ne peut pas être vide"),
                        "BAD_REQUEST",
                        400
                ));
            }

            FileEntity uploadedFile = fileService.uploadFile(file);
            FileResponseDTO responseDto = fileMapper.toResponseDto(uploadedFile);

            return ResponseEntity.status(201).body(new ApiResponse<>(
                    true,
                    "Fichier téléchargé avec succès",
                    responseDto,
                    null,
                    "SUCCESS",
                    201
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(
                    false,
                    "Erreur lors du téléchargement du fichier",
                    null,
                    List.of(e.getMessage()),
                    "ERROR",
                    500
            ));
        }
    }

    @Override
    @GetMapping("/{id}/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long id) {
        try {
            FileEntity fileEntity = fileService.getById(id);
            byte[] data = fileService.downloadFile(id);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileEntity.getOriginalFileName() + "\"")
                    .contentType(MediaType.parseMediaType(fileEntity.getContentType()))
                    .contentLength(fileEntity.getSize())
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FileResponseDTO>>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "searchQuery", required = false) String searchQuery) {
        try {
            // Créer un objet Pageable pour la pagination
            Pageable pageable = PageRequest.of(page, size);

            // Si searchQuery est présent, filtrer les fichiers
            Page<FileEntity> filePage;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                filePage = fileService.searchFiles(searchQuery, pageable);
            } else {
                filePage = fileService.getAllFiles(pageable);
            }

            // Mapper les entités vers des DTOs
            List<FileResponseDTO> responseDtos = filePage.getContent().stream()
                    .map(fileMapper::toResponseDto)
                    .collect(Collectors.toList());

            // Créer la réponse avec les métadonnées de la pagination
            ApiResponse<List<FileResponseDTO>> apiResponse = new ApiResponse<>(
                    true,
                    "Fichiers récupérés avec succès",
                    responseDtos,
                    null,
                    "SUCCESS",
                    200
            );

            // Ajouter les informations de pagination à la réponse
            apiResponse.setPage(page);
            apiResponse.setSize(size);
            apiResponse.setTotalElements(filePage.getTotalElements());
            apiResponse.setTotalPages(filePage.getTotalPages());

            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(
                    false,
                    "Erreur lors de la récupération des fichiers",
                    null,
                    List.of(e.getMessage()),
                    "ERROR",
                    500
            ));
        }
    }

    // Ajout de la méthode DELETE /files/{id}
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            return super.delete(id);  // Hérite de la méthode générique de BaseControllerImpl
        } catch (Exception e) {
            ApiResponse<Void> apiResponse = new ApiResponse<>(false, "Erreur interne", null, List.of(e.getMessage()), "INTERNAL_ERROR", 500);
            return ResponseEntity.status(500).body(apiResponse);
        }
    }
}