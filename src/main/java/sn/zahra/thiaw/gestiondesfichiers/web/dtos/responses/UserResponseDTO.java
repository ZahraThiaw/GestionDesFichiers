package sn.zahra.thiaw.gestiondesfichiers.web.dtos.responses;

import lombok.Data;
import sn.zahra.thiaw.gestiondesfichiers.datas.entities.UserEntity;

@Data
public class UserResponseDTO {

    private Long id;

    private String nom;

    private String prenom;

    private String email;

    private UserEntity.Role role;
}
