package org.playground.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CorsController {

    /**
     * The index.html file makes a call to 127.0.0.1:8080
     */
    @GetMapping("/cors")
    public String cors() {
        return "index.html";
    }
}
