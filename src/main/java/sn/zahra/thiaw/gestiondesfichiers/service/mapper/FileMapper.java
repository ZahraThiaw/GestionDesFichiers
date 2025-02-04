package sn.zahra.thiaw.gestiondesfichiers.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sn.zahra.thiaw.gestiondesfichiers.domain.entity.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.model.requests.FileRequestDTO;
import sn.zahra.thiaw.gestiondesfichiers.model.responses.FileResponseDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper extends GenericMapper<FileEntity, FileRequestDTO, FileResponseDTO> {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Override
    FileEntity toEntity(FileRequestDTO requestDto);

    @Override
    FileResponseDTO toResponseDto(FileEntity entity);

    @Override
    List<FileResponseDTO> toResponseDtoList(List<FileEntity> entities);
}
