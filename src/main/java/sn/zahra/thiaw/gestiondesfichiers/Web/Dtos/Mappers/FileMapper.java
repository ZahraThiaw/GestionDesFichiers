package sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Requests.FileRequestDTO;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.FileResponseDTO;

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
