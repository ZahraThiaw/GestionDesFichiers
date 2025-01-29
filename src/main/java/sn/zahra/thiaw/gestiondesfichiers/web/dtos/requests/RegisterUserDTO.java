package sn.zahra.thiaw.gestiondesfichiers.web.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {

    @Schema(description = "Le nom de l'utilisateur")
    private String nom;

    @Schema(description = "Le prenom de l'utilisateur")
    private String prenom;

    @Schema(description = "L'email de l'utilisateur")
    private String email;

    @Schema(description = "Le password de l'utilisateur")
    private String password;
}