
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
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.Mappers.FileMapper;
import sn.zahra.thiaw.gestiondesfichiers.Services.FileService;
import sn.zahra.thiaw.gestiondesfichiers.Web.Controllers.FileController;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.FileResponseDTO;
import sn.zahra.thiaw.gestiondesfichiers.Web.Filters.ApiResponse;

import java.util.List;
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
    public ResponseEntity<ApiResponse<FileResponseDTO>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "storageType", defaultValue = "LOCAL") StorageType storageType) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(
                        false,
                        "File is empty",
                        null,
                        List.of("File cannot be empty"),
                        "BAD_REQUEST",
                        400
                ));
            }

            FileEntity uploadedFile = fileService.uploadFile(file, storageType);
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
            Pageable pageable = PageRequest.of(page, size);
            Page<FileEntity> filePage;

            if (searchQuery != null && !searchQuery.isEmpty()) {
                filePage = fileService.searchFiles(searchQuery, pageable);
            } else {
                filePage = fileService.getAllFiles(pageable);
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

            apiResponse.setPage(page);
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