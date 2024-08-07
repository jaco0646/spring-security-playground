package org.playground.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureController {
    public static final String BASIC = "/secure/basic";
    public static final String HEADER = "/secure/header";

    @GetMapping("/")
//    @CrossOrigin("http://localhost:8080")  // Allow access from the CorsController index.html script.
    public String home() {
        return "Hello World";
    }

    @GetMapping(BASIC)
    public String basic() {
        return "Basic security.";
    }

    @GetMapping(HEADER)
    public String header() {
        return "Header security.";
    }
}
