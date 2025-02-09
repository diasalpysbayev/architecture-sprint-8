package com.example.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final KeycloakService keycloakService;

    public ApiController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @GetMapping("/reports")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> get(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !keycloakService.validateToken(token)) {
            return ResponseEntity.status(401).body("UNAUTHORIZED");
        }
        return ResponseEntity.ok().body("SUCCESS");
    }

}
