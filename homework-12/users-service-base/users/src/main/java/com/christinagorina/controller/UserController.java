package com.christinagorina.controller;

import com.christinagorina.model.Answer;
import com.christinagorina.model.User;
import com.christinagorina.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<String> indexPage() {
        System.out.println("qwe / method");
        return new ResponseEntity<>("Users service main page success", HttpStatus.OK);
    }

    @GetMapping("/api/v1/user/name/{name}")
    public User findByName(@PathVariable("name") String name, @RequestHeader("x-auth-user") String xAuthUser) throws IllegalAccessException {
        System.out.println("qwe findByName method");
        System.out.println("qwe findByName name = " + name);
        System.out.println("qwe findByName xAuthUser = " + xAuthUser);
        Optional.ofNullable(xAuthUser).filter(n -> n.equals(name)).orElseThrow(IllegalAccessException::new);
        System.out.println("qwe findByName after check");
        return userRepository.findByName(name);
    }

    @PostMapping("/api/v1/user")
    public User create(@RequestBody User user) {
        System.out.println("qwe create method");
        System.out.println("qwe findByName user = " + user);
        Optional.ofNullable(user).filter(u -> Objects.isNull(u.getId())).orElseThrow(IllegalArgumentException::new);
        System.out.println("qwe create after check");
        return userRepository.save(user);
    }

    @PutMapping("/api/v1/user/name/{name}")
    public User update(@RequestBody User updateUserData, @PathVariable String name, @RequestHeader("x-auth-user") String xAuthUser) throws IllegalAccessException {
        System.out.println("qwe update method");
        System.out.println("qwe update updateUserData = " + updateUserData);
        System.out.println("qwe update name = " + name);
        System.out.println("qwe update xAuthUser = " + xAuthUser);
        Optional.ofNullable(xAuthUser).filter(n -> n.equals(name)).orElseThrow(IllegalAccessException::new);
        System.out.println("qwe update after check1");
        User existingUser = Optional.of(userRepository.findByName(name)).orElseThrow(IllegalArgumentException::new);
        System.out.println("qwe update after check2");
        if(Objects.isNull(updateUserData.getPassword())){
            System.out.println("qwe update getPassword = " + updateUserData.getPassword());
            updateUserData.setPassword(existingUser.getPassword());
        }

        if(Objects.isNull(updateUserData.getName())){
            System.out.println("qwe update getName = " + updateUserData.getName());
            updateUserData.setName(existingUser.getName());
        }
        System.out.println("qwe update getId = " + existingUser.getId());
        updateUserData.setId(existingUser.getId());
        System.out.println("qwe update updateUserData = " + updateUserData);
        return userRepository.save(updateUserData);
    }

    @GetMapping("/health")
    public ResponseEntity<Answer> health() {
        System.out.println("qwe health method");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Answer("OK"));
    }

}


