package ru.edjll.backend.service;

import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.resource.AuthorizationResource;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthorizationResource resource;
    private final String username = "admin";
    private final String password = "admin";

    public AuthService() {
        AuthzClient client = AuthzClient.create();
        this.resource = client.authorization(username, password);
    }

    public String getAdminToken() {
        AuthorizationRequest request = new AuthorizationRequest();
        AuthorizationResponse response = resource.authorize(request);
        return response.getToken();
    }
}
