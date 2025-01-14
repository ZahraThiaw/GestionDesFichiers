package sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.UserEntity;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.UserResponseDTO;

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
