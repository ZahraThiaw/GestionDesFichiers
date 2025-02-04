package sn.zahra.thiaw.gestiondesfichiers.web;


import sn.zahra.thiaw.gestiondesfichiers.domain.entity.UserEntity;
import sn.zahra.thiaw.gestiondesfichiers.model.responses.UserResponseDTO;

public interface UserController extends BaseController<UserEntity, Long, UserResponseDTO> {

    //ResponseEntity<ApiResponse<UserResponseDTO>> getConnectedUserProfile();
}
