package sn.zahra.thiaw.gestiondesfichiers.web.controllers;


import sn.zahra.thiaw.gestiondesfichiers.datas.entities.UserEntity;
import sn.zahra.thiaw.gestiondesfichiers.web.dtos.responses.UserResponseDTO;

public interface UserController extends BaseController<UserEntity, Long, UserResponseDTO> {

    //ResponseEntity<ApiResponse<UserResponseDTO>> getConnectedUserProfile();
}
