package sn.zahra.thiaw.gestiondesfichiers.Web.Controllers;


import org.springframework.http.ResponseEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.UserEntity;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.UserResponseDTO;
import sn.zahra.thiaw.gestiondesfichiers.Filters.ApiResponse;

public interface UserController extends BaseController<UserEntity, Long, UserResponseDTO> {

    ResponseEntity<ApiResponse<UserResponseDTO>> getConnectedUserProfile();
}
