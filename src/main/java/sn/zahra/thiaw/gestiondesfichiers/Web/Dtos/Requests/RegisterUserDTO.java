package sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {
    private String nom;
    private String prenom;
    private String email;
    private String password;
}