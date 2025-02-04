package sn.zahra.thiaw.gestiondesfichiers.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sn.zahra.thiaw.gestiondesfichiers.domain.entity.UserEntity;
import sn.zahra.thiaw.gestiondesfichiers.model.responses.UserResponseDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<UserEntity, Void, UserResponseDTO> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Override
    UserResponseDTO toResponseDto(UserEntity entity);

    @Override
    default List<UserResponseDTO> toResponseDtoList(List<UserEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    default UserEntity toEntity(Void createDto) {
        throw new UnsupportedOperationException("toEntity is not implemented for UserMapper.");
    }
}
