package ru.edjll.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("Hello, world!");
    }
}
