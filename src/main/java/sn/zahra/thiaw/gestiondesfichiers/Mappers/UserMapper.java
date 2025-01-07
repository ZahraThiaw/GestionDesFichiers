package sn.zahra.thiaw.gestiondesfichiers.Mappers;


import org.springframework.stereotype.Component;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.UserEntity;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.UserResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper implements GenericMapper<UserEntity, Void, UserResponseDTO> {

    @Override
    public UserResponseDTO toResponseDto(UserEntity entity) {
        if (entity == null) return null;

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setNom(entity.getNom());
        responseDTO.setPrenom(entity.getPrenom());
        responseDTO.setEmail(entity.getEmail());
        responseDTO.setRole(entity.getRole());
        return responseDTO;
    }

    @Override
    public UserEntity toEntity(Void createDto) {
        throw new UnsupportedOperationException("toEntity is not implemented for UserMapper.");
    }

    @Override
    public List<UserResponseDTO> toResponseDtoList(List<UserEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
