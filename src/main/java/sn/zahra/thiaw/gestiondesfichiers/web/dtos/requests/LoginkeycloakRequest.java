package sn.zahra.thiaw.gestiondesfichiers.web.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginkeycloakRequest {

    @Schema(description = "Le username de l'utilisateur")
    private String username;

    @Schema(description = "Le password de l'utilisateur")
    private String password;


}