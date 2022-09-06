package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otus.spring.model.User;
import ru.otus.spring.security.AuthorizedUser;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-User", username);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<User> respEntity = restTemplate.exchange("http://localhost:8082/api/v1/user/name/" + username, HttpMethod.GET, entity, User.class);
        User user = respEntity.getBody();
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " is not found");
        }
        return new AuthorizedUser(user);
    }
}