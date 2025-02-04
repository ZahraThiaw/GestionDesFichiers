package sn.zahra.thiaw.gestiondesfichiers.web.impl;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.zahra.thiaw.gestiondesfichiers.exception.BadRequestException;
import sn.zahra.thiaw.gestiondesfichiers.exception.ResourceNotFoundException;
import sn.zahra.thiaw.gestiondesfichiers.exception.UnauthorizedException;
import sn.zahra.thiaw.gestiondesfichiers.exception.ValidationException;
import sn.zahra.thiaw.gestiondesfichiers.web.BaseController;
import sn.zahra.thiaw.gestiondesfichiers.config.filter.ApiResponse;
import sn.zahra.thiaw.gestiondesfichiers.service.mapper.GenericMapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseControllerImpl<E, ID, R> implements BaseController<E, ID, R> {

    private final sn.zahra.thiaw.gestiondesfichiers.service.BaseService<E, ID> service;
    private final GenericMapper<E, ?, R> mapper;

    public BaseControllerImpl(sn.zahra.thiaw.gestiondesfichiers.service.BaseService<E, ID> service,
                              GenericMapper<E, ?, R> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<ApiResponse<R>> create(E entity) {
        try {
            E createdEntity = service.create(entity);
            R responseDto = mapper.toResponseDto(createdEntity);
            ApiResponse<R> apiResponse = new ApiResponse<>(true, "Entity created successfully", responseDto, null, "SUCCESS", HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } catch (ValidationException e) {
            ApiResponse<R> apiResponse = new ApiResponse<>(false, "Validation error: " + e.getMessage(), null, List.of(e.getMessage()), "VALIDATION_ERROR", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(apiResponse);
        } catch (BadRequestException e) {
            ApiResponse<R> apiResponse = new ApiResponse<>(false, "Bad request: " + e.getMessage(), null, List.of(e.getMessage()), "BAD_REQUEST", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<R>> update(ID id, E entity) {
        try {
            E updatedEntity = service.update(id, entity);
            R responseDto = mapper.toResponseDto(updatedEntity);
            ApiResponse<R> apiResponse = new ApiResponse<>(true, "Entity updated successfully", responseDto, null, "SUCCESS", HttpStatus.OK.value());
            return ResponseEntity.ok(apiResponse);
        } catch (ResourceNotFoundException e) {
            ApiResponse<R> apiResponse = new ApiResponse<>(false, "Entity not found with ID: " + id, null, List.of(e.getMessage()), "NOT_FOUND", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        } catch (ValidationException e) {
            ApiResponse<R> apiResponse = new ApiResponse<>(false, "Validation error: " + e.getMessage(), null, List.of(e.getMessage()), "VALIDATION_ERROR", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<R>> getById(ID id) {
        try {
            E foundEntity = service.getById(id);
            R responseDto = mapper.toResponseDto(foundEntity);
            ApiResponse<R> apiResponse = new ApiResponse<>(true, "Entity retrieved successfully", responseDto, null, "SUCCESS", HttpStatus.OK.value());
            return ResponseEntity.ok(apiResponse);
        } catch (ResourceNotFoundException e) {
            ApiResponse<R> apiResponse = new ApiResponse<>(false, "Entity not found with ID: " + id, null, List.of(e.getMessage()), "NOT_FOUND", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<List<R>>> getAll() {
            List<E> allEntities = service.getAll();
            List<R> responseDtos = allEntities.stream().map(mapper::toResponseDto).collect(Collectors.toList());
            ApiResponse<List<R>> apiResponse = new ApiResponse<>(true, "Entities retrieved successfully", responseDtos, null, "SUCCESS", HttpStatus.OK.value());
            return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> delete(ID id) {
        try {
            service.delete(id);
            ApiResponse<Void> apiResponse = new ApiResponse<>(true, "Entity deleted successfully", null, null, "SUCCESS", HttpStatus.NO_CONTENT.value());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> apiResponse = new ApiResponse<>(false, "Entity not found with ID: " + id, null, List.of(e.getMessage()), "NOT_FOUND", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        } catch (UnauthorizedException e) {
            ApiResponse<Void> apiResponse = new ApiResponse<>(false, "Unauthorized to delete entity with ID: " + id, null, List.of(e.getMessage()), "UNAUTHORIZED", HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
        }
    }
}
