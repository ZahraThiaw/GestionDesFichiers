package sn.zahra.thiaw.gestiondesfichiers.web.dtos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sn.zahra.thiaw.gestiondesfichiers.datas.entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.web.dtos.requests.FileRequestDTO;
import sn.zahra.thiaw.gestiondesfichiers.web.dtos.responses.FileResponseDTO;

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
