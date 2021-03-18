package ru.edjll.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/anonymous")
    public ResponseEntity<String> anonymous() {
        return ResponseEntity.ok().body("Hello, anonymous!");
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/user")
    public ResponseEntity<String> user(Principal principal) {
        return ResponseEntity.ok().body("Hello, user, " + principal.getName() + "!");
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/admin")
    public ResponseEntity<String> admin(Principal principal) {
        return ResponseEntity.ok().body("Hello, admin, " + principal.getName() + "!");
    }
}
