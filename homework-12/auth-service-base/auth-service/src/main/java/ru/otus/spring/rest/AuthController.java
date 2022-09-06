package ru.otus.spring.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;

@Controller
public class AuthController {

    @GetMapping("/")
    public ResponseEntity<String> indexPage() {
        return new ResponseEntity<>("Auth service main page success", HttpStatus.OK);
    }

    @GetMapping("/insystem")
    public ResponseEntity<String> inSystem() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaders responseHeaders= new HttpHeaders();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            responseHeaders.setLocation(URI.create("/login"));
            return new ResponseEntity<>("User not authenticated", responseHeaders, HttpStatus.MOVED_PERMANENTLY);
        } else {
            System.out.println("qwe1 getName = " + authentication.getName());
            responseHeaders.set("X-Auth-User", authentication.getName());
            return new ResponseEntity<>("User authenticated", responseHeaders, HttpStatus.OK);
        }
    }
}
