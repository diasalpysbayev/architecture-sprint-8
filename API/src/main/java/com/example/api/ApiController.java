package com.example.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/reports")
    public ResponseEntity<String> get(@RequestHeader(value = "Authorization", required = false) String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("UNAUTHORIZED");
        }
        return ResponseEntity.ok().body("SUCCESS");
    }

}
