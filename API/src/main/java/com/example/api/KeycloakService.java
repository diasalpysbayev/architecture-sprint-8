package com.example.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class KeycloakService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RestTemplate restTemplate;

    public KeycloakService(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }

    public boolean validateToken(String token) {
        log.info("token {}", token);
        token = token.substring(7);

        String url = "http://localhost:8080/realms/reports-realm/protocol/openid-connect/token/introspect";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.setBasicAuth("reports-api", "oNwoLQdvJAvRcL89SydqCWCe5ry1jMgq");
        String auth = Base64.getEncoder()
                .encodeToString("reports-api:oNwoLQdvJAvRcL89SydqCWCe5ry1jMgq".getBytes());
        headers.add("Authorization", "Basic " + auth);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", token);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        log.info("params {}", params);
        log.info("headers {}", headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        log.info("response {}", response);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return Boolean.TRUE.equals(response.getBody().get("active"));
        }

        return false;
    }

//    public static void main(String[] args) {
//        System.out.println("");
//    }
//    curl -X POST "http://localhost:8080/realms/reports-realm/protocol/openid-connect/token/introspect" \
//            -H "Content-Type: application/x-www-form-urlencoded" \
//            -d "client_id=reports-api" \
//            -d "client_secret=oNwoLQdvJAvRcL89SydqCWCe5ry1jMgq" \
//            -d "token=eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJhT3NRMWQzd3BIV2ptWnZfVzVXNkZ2WUFDLXBpQjhSRmxTQnhvTzBTaHFnIn0.eyJleHAiOjE3Mzk3MTIxNzgsImlhdCI6MTczOTcxMTg3OCwiYXV0aF90aW1lIjoxNzM5NzExODY4LCJqdGkiOiI3NDU4YTAyNi05ZTU1LTRmYWItYTA0Zi1hZDU1OGYxMzkwNmQiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvcmVhbG1zL3JlcG9ydHMtcmVhbG0iLCJzdWIiOiIzNzI3ZjE1Mi1jOTJkLTQ2MmUtODY5YS1lYWRjYWNhZjFhYjMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJyZXBvcnRzLWZyb250ZW5kIiwibm9uY2UiOiI4NjdiZDBiMi1hMDgxLTQ3MTYtYmE3OS0xZWRmYTZhNjRiOWMiLCJzZXNzaW9uX3N0YXRlIjoiYTM0MjM4ZDQtZTMyZi00OWZhLTkwMTUtMDhjMzkzMGMzMjQzIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjMwMDAiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInByb3RoZXRpY191c2VyIl19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwic2lkIjoiYTM0MjM4ZDQtZTMyZi00OWZhLTkwMTUtMDhjMzkzMGMzMjQzIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiUHJvdGhldGljIE9uZSIsInByZWZlcnJlZF91c2VybmFtZSI6InByb3RoZXRpYzEiLCJnaXZlbl9uYW1lIjoiUHJvdGhldGljIiwiZmFtaWx5X25hbWUiOiJPbmUiLCJlbWFpbCI6InByb3RoZXRpYzFAZXhhbXBsZS5jb20ifQ.ddlPBuTQTPl4jVuMD93Uit2BhTqnOK-9TGdPCEJoLtTFh29CpCoA8kJa_JRBAZcuAt-oPJ9-6R1YCkThegx1Jj3XNH7pEPmevNRKYJgNa7kAw8IbWdZyXfAtk0gbjysr_Z9k2pCuZ9vE8b6UJkMN087flRRVVeLR0Mo509eMVNZCmUxpghkxnnh2vm1VuEOQdGTYdde2VmZ1IGJrwwL5Wd4Bp5oz52fGVy6J6VxYRLWTCviw4H0-tn8RLHMQhQFs2PYpKCr8D0e_iom9NfhU3137TM5Cd-632x8n17fCaSBRSORjwZ8ztN6s5_tIcZGn3UdZpUwnDR4opslvchzS5A"

//
//    curl -X GET http://localhost:8000/reports -i \
//            -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJhT3NRMWQzd3BIV2ptWnZfVzVXNkZ2WUFDLXBpQjhSRmxTQnhvTzBTaHFnIn0.eyJleHAiOjE3Mzk3MTIxNzgsImlhdCI6MTczOTcxMTg3OCwiYXV0aF90aW1lIjoxNzM5NzExODY4LCJqdGkiOiI3NDU4YTAyNi05ZTU1LTRmYWItYTA0Zi1hZDU1OGYxMzkwNmQiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvcmVhbG1zL3JlcG9ydHMtcmVhbG0iLCJzdWIiOiIzNzI3ZjE1Mi1jOTJkLTQ2MmUtODY5YS1lYWRjYWNhZjFhYjMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJyZXBvcnRzLWZyb250ZW5kIiwibm9uY2UiOiI4NjdiZDBiMi1hMDgxLTQ3MTYtYmE3OS0xZWRmYTZhNjRiOWMiLCJzZXNzaW9uX3N0YXRlIjoiYTM0MjM4ZDQtZTMyZi00OWZhLTkwMTUtMDhjMzkzMGMzMjQzIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjMwMDAiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInByb3RoZXRpY191c2VyIl19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwic2lkIjoiYTM0MjM4ZDQtZTMyZi00OWZhLTkwMTUtMDhjMzkzMGMzMjQzIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiUHJvdGhldGljIE9uZSIsInByZWZlcnJlZF91c2VybmFtZSI6InByb3RoZXRpYzEiLCJnaXZlbl9uYW1lIjoiUHJvdGhldGljIiwiZmFtaWx5X25hbWUiOiJPbmUiLCJlbWFpbCI6InByb3RoZXRpYzFAZXhhbXBsZS5jb20ifQ.ddlPBuTQTPl4jVuMD93Uit2BhTqnOK-9TGdPCEJoLtTFh29CpCoA8kJa_JRBAZcuAt-oPJ9-6R1YCkThegx1Jj3XNH7pEPmevNRKYJgNa7kAw8IbWdZyXfAtk0gbjysr_Z9k2pCuZ9vE8b6UJkMN087flRRVVeLR0Mo509eMVNZCmUxpghkxnnh2vm1VuEOQdGTYdde2VmZ1IGJrwwL5Wd4Bp5oz52fGVy6J6VxYRLWTCviw4H0-tn8RLHMQhQFs2PYpKCr8D0e_iom9NfhU3137TM5Cd-632x8n17fCaSBRSORjwZ8ztN6s5_tIcZGn3UdZpUwnDR4opslvchzS5A"
}
