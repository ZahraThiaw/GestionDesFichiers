// FileMapper.java
package sn.zahra.thiaw.gestiondesfichiers.Mappers;

import org.springframework.stereotype.Component;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Requests.FileRequestDTO;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.FileResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileMapper implements GenericMapper<FileEntity, FileRequestDTO, FileResponseDTO> {

    @Override
    public FileEntity toEntity(FileRequestDTO requestDto) {
        FileEntity entity = new FileEntity();
        // Les propriétés seront définies dans le service
        return entity;
    }

    @Override
    public FileResponseDTO toResponseDto(FileEntity entity) {
        FileResponseDTO responseDto = new FileResponseDTO();
        responseDto.setId(entity.getId());
        responseDto.setFileName(entity.getFileName());
        responseDto.setOriginalFileName(entity.getOriginalFileName());
        responseDto.setContentType(entity.getContentType());
        responseDto.setSize(entity.getSize());
        responseDto.setCreatedAt(entity.getCreatedAt());
        return responseDto;
    }

    @Override
    public List<FileResponseDTO> toResponseDtoList(List<FileEntity> entities) {
        return entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
