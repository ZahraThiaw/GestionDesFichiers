package sn.zahra.thiaw.gestiondesfichiers.Services.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Requests.LoginkeycloakRequest;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.LoginResponse;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.LoginkeycloakResponse;

@Service
public class KeycloakAuthService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthUrl;

    @Value("${keycloak.client-id}")
    private String clientId;

    private final RestTemplate restTemplate;

    public KeycloakAuthService() {
        this.restTemplate = new RestTemplate();
    }

    public LoginkeycloakResponse authenticate(LoginkeycloakRequest loginRequest) {
        String tokenUrl = keycloakAuthUrl + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", clientId);
        map.add("username", loginRequest.getUsername());
        map.add("password", loginRequest.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<LoginkeycloakResponse> response = restTemplate.postForEntity(
                tokenUrl,
                request,
                LoginkeycloakResponse.class
        );

        return response.getBody();
    }
}