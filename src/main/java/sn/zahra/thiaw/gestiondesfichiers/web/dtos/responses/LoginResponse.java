package sn.zahra.thiaw.gestiondesfichiers.web.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String token;

    private long expiresIn;

}