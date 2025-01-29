package sn.zahra.thiaw.gestiondesfichiers.web.controllers.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.zahra.thiaw.gestiondesfichiers.datas.entities.UserEntity;
import sn.zahra.thiaw.gestiondesfichiers.services.impl.AuthenticationService;
import sn.zahra.thiaw.gestiondesfichiers.services.impl.KeycloakAuthService;
import sn.zahra.thiaw.gestiondesfichiers.web.dtos.requests.LoginkeycloakRequest;
import sn.zahra.thiaw.gestiondesfichiers.web.dtos.requests.RegisterUserDTO;
import sn.zahra.thiaw.gestiondesfichiers.filters.ApiResponse;
import sn.zahra.thiaw.gestiondesfichiers.web.dtos.responses.LoginkeycloakResponse;

import java.util.Collections;

@RequestMapping("/auth")
@RestController
@Tag(name = "Auth", description = "API pour gérer les utilisateurs")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final KeycloakAuthService keycloakAuthService;

    public AuthenticationController(
            AuthenticationService authenticationService,
            KeycloakAuthService keycloakAuthService) {
        this.authenticationService = authenticationService;
        this.keycloakAuthService = keycloakAuthService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserEntity>> register(@RequestBody RegisterUserDTO registerUserDTO) {
        UserEntity registeredUser = authenticationService.signup(registerUserDTO);
        ApiResponse<UserEntity> response = new ApiResponse<>(
                true,
                "Utilisateur enregistré avec succès",
                registeredUser,
                Collections.emptyList(),
                "OK",
                201
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/keycloak")
    public ResponseEntity<ApiResponse<LoginkeycloakResponse>> login(@RequestBody LoginkeycloakRequest loginRequest) {
        // Use the injected instance instead of static call
        LoginkeycloakResponse loginResponse = keycloakAuthService.authenticate(loginRequest);

        ApiResponse<LoginkeycloakResponse> response = new ApiResponse<>(
                true,
                "Authentification réussie",
                loginResponse,
                Collections.emptyList(),
                "OK",
                200
        );

        return ResponseEntity.ok(response);
    }



//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<LoginResponse>> authenticate(@RequestBody LoginUserDto loginUserDto) {
//        UserEntity authenticatedUser = authenticationService.authenticate(loginUserDto);
//        String jwtToken = jwtService.generateToken(authenticatedUser);
//        LoginResponse loginResponse = new LoginResponse();
//        loginResponse.setToken(jwtToken);
//
//        ApiResponse<LoginResponse> response = new ApiResponse<>(
//                true,
//                "Authentification réussie",
//                loginResponse,
//                Collections.emptyList(),
//                "OK",
//                200
//        );
//        return ResponseEntity.ok(response);
//    }


}
