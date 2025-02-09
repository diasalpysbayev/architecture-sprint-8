package com.example.api;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KeycloakService {

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validateToken(String token) {
        String url = "http://localhost:8080/realms/reports-realm/protocol/openid-connect/token/introspect";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("reports-api", "oNwoLQdvJAvRcL89SydqCWCe5ry1jMgq");

        Map<String, String> params = new HashMap<>();
        params.put("token", token);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return Boolean.TRUE.equals(response.getBody().get("active"));
        }

        return false;
    }
}
