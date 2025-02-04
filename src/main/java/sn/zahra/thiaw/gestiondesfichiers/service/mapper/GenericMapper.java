package sn.zahra.thiaw.gestiondesfichiers.service.mapper;

import java.util.List;

public interface GenericMapper<E, ReqDto, ResDto> {

    E toEntity(ReqDto requestDto);

    ResDto toResponseDto(E entity);

    List<ResDto> toResponseDtoList(List<E> entities);
}

