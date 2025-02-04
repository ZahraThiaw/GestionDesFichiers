package sn.zahra.thiaw.gestiondesfichiers.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sn.zahra.thiaw.gestiondesfichiers.model.requests.LoginkeycloakRequest;
import sn.zahra.thiaw.gestiondesfichiers.model.responses.LoginkeycloakResponse;

@Service
public class KeycloakAuthService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public KeycloakAuthService() {
        this.restTemplate = new RestTemplate();
    }

    public LoginkeycloakResponse authenticate(LoginkeycloakRequest loginRequest) {
        String tokenUrl = keycloakAuthUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
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