package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        System.out.println("loadUserByUsername username = " + username);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-User", username);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        //ResponseEntity<User> respEntity = restTemplate.exchange("http://localhost:8096/api/v1/user/name/" + username, HttpMethod.GET, entity, User.class); // обращение к userservice в кубе, arch.homework прописан в hosts файле, localhost:8096 можно заменить на него
        ResponseEntity<User> respEntity = restTemplate.exchange("http://users-service.finalwork-namespace.svc.cluster.local/api/v1/user/name/" + username, HttpMethod.GET, entity, User.class);
        User user = respEntity.getBody();
        if (user == null) {
            System.out.println("loadUserByUsername user == null");
            throw new UsernameNotFoundException("User " + username + " is not found");
        }
        System.out.println("loadUserByUsername user = " + user);
        return new AuthorizedUser(user);
    }
}