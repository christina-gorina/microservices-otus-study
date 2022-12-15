package ru.otus.spring.controller;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.otus.spring.model.User;

@RestController
public class AuthController {

    @GetMapping("/")
    public ResponseEntity<String> indexPage() {
        System.out.println("method /");
        return new ResponseEntity<>("Auth service main page success", HttpStatus.OK);
    }

    @GetMapping("/insystem")
    public ResponseEntity<String> inSystem() {
        System.out.println("insystem method");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaders responseHeaders= new HttpHeaders();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            System.out.println("insystem anonymousUser");
            return new ResponseEntity<>("User not authenticated", responseHeaders, HttpStatus.UNAUTHORIZED);
        } else {
            System.out.println("insystem loginUser = " + authentication.getName());
            responseHeaders.set("X-Auth-User", authentication.getName());
            return new ResponseEntity<>("User authenticated", responseHeaders, HttpStatus.OK);
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody User user) {
        System.out.println("authorisation user = " + user);
        if (user == null || user.getName() == null || user.getPassword() == null) {
            System.out.println("authorisation wrong user data");
            throw new UsernameNotFoundException("Wrong user data");
        }
        user.setId(null);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println("authorisation headers = " + headers);
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        System.out.println(" authorisation request = " + request);
        User registeredUser = restTemplate.postForObject("http://users-service.finalwork-namespace.svc.cluster.local/api/v1/user", request, User.class);
        //User registeredUser = restTemplate.postForObject("http://localhost:8096/api/v1/user", request, User.class); // обращение к userservice запущенному через идею
        System.out.println("authorisation registeredUser = " + registeredUser);
        if (registeredUser == null) {
            System.out.println("authorisation registeredUser is null");
            throw new UsernameNotFoundException("RegisteredUser is null");
        }
        return new ResponseEntity<>("Registration user with name " + registeredUser.getName() + " success", HttpStatus.OK);
    }

}
