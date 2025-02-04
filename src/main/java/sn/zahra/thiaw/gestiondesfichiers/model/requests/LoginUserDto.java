package sn.zahra.thiaw.gestiondesfichiers.model.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginUserDto {

    @Schema(description = "L'email de l'utilisateur")
    private String email;

    @Schema(description = "Le password de l'utilisateur")
    private String password;


}