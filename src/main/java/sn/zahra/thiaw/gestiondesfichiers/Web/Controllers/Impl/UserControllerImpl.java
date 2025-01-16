package sn.zahra.thiaw.gestiondesfichiers.Web.Controllers.Impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.UserEntity;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Mappers.GenericMapper;
import sn.zahra.thiaw.gestiondesfichiers.Services.UserService;
import sn.zahra.thiaw.gestiondesfichiers.Web.Controllers.UserController;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.UserResponseDTO;
import sn.zahra.thiaw.gestiondesfichiers.Filters.ApiResponse;

import java.util.Collections;
import java.util.List;

@RequestMapping("/users")
@RestController
public class UserControllerImpl extends BaseControllerImpl<UserEntity, Long, UserResponseDTO> implements UserController {

    private final UserService userService;

    public UserControllerImpl(UserService userService,
                              GenericMapper<UserEntity, ?, UserResponseDTO> userMapper) {
        super(userService, userMapper);
        this.userService = userService;
    }

//    // Endpoint pour récupérer le profil de l'utilisateur connecté
//    @Override
//    @GetMapping("/profile")
//    public ResponseEntity<ApiResponse<UserResponseDTO>> getConnectedUserProfile() {
//        UserResponseDTO profile = userService.getConnectedUserProfile();
//        ApiResponse<UserResponseDTO> response = new ApiResponse<>(
//                true,
//                "Profil utilisateur récupéré avec succès",
//                profile,
//                Collections.emptyList(),
//                "OK",
//                200
//        );
//        return ResponseEntity.ok(response);
//    }

    // Endpoint pour lister tous les utilisateurs (hérité de BaseControllerImpl)
    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAll() {
        return super.getAll();  // Utilise la méthode générique héritée de BaseControllerImpl
    }

    // Endpoint pour récupérer un utilisateur par son ID (hérité de BaseControllerImpl)
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getById(@PathVariable Long id) {
        return super.getById(id);  // Utilise la méthode générique héritée de BaseControllerImpl
    }

    // Endpoint pour mettre à jour un utilisateur (hérité de BaseControllerImpl)
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> update(@PathVariable Long id, @RequestBody UserEntity userEntity) {
        return super.update(id, userEntity);  // Utilise la méthode générique héritée de BaseControllerImpl
    }

    // Endpoint pour supprimer un utilisateur (hérité de BaseControllerImpl)
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        return super.delete(id);  // Utilise la méthode générique héritée de BaseControllerImpl
    }
}
