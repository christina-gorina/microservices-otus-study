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
        System.out.println("qwe loadUserByUsername method");
        System.out.println("qwe loadUserByUsername username = " + username);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-User", username);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        //ResponseEntity<User> respEntity = restTemplate.exchange("http://arch.homework/api/v1/user/name/" + username, HttpMethod.GET, entity, User.class); // обращение к userservice в кубе, arch.homework прописан в hosts файле
        ResponseEntity<User> respEntity = restTemplate.exchange("http://usersserver.homework-12.svc.cluster.local/api/v1/user/name/" + username, HttpMethod.GET, entity, User.class);
        User user = respEntity.getBody();
        if (user == null) {
            System.out.println("qwe loadUserByUsername user == null");
            throw new UsernameNotFoundException("User " + username + " is not found");
        }
        System.out.println("qwe loadUserByUsername user = " + user);
        return new AuthorizedUser(user);
    }
}