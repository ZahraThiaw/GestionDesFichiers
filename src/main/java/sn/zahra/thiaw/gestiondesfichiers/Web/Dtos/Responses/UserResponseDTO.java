package sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses;

import lombok.Data;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.UserEntity;

@Data
public class UserResponseDTO {

    private Long id;

    private String nom;

    private String prenom;

    private String email;

    private UserEntity.Role role;
}
