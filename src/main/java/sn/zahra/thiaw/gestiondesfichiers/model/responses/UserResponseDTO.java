package sn.zahra.thiaw.gestiondesfichiers.model.responses;

import lombok.Data;
import sn.zahra.thiaw.gestiondesfichiers.domain.entity.UserEntity;

@Data
public class UserResponseDTO {

    private Long id;

    private String nom;

    private String prenom;

    private String email;

    private UserEntity.Role role;
}
