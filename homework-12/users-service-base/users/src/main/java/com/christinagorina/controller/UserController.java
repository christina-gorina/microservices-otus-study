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
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<String> indexPage() {
        return new ResponseEntity<>("Users service main page success", HttpStatus.OK);
    }

    @GetMapping("/api/v1/user/name/{name}")
    public User findByName(@PathVariable("name") String name, @RequestHeader("x-auth-user") String xAuthUser) throws IllegalAccessException {
        Optional.ofNullable(xAuthUser).filter(n -> n.equals(name)).orElseThrow(IllegalAccessException::new);
        return userRepository.findByName(name);
    }

    @PostMapping("/api/v1/user")
    public User create(@RequestBody User user) {
        Optional.ofNullable(user).filter(u -> Objects.isNull(u.getId())).orElseThrow(IllegalArgumentException::new);
        return userRepository.save(user);
    }

    @PutMapping("/api/v1/user/name/{name}")
    public User update(@RequestBody User updateUserData, @PathVariable String name, @RequestHeader("x-auth-user") String xAuthUser) throws IllegalAccessException {
        Optional.ofNullable(xAuthUser).filter(n -> n.equals(name)).orElseThrow(IllegalAccessException::new);
        User existingUser = Optional.of(userRepository.findByName(name)).orElseThrow(IllegalArgumentException::new);
        if(Objects.isNull(updateUserData.getPassword())){
            updateUserData.setPassword(existingUser.getPassword());
        }

        if(Objects.isNull(updateUserData.getName())){
            updateUserData.setName(existingUser.getName());
        }
        updateUserData.setId(existingUser.getId());
        return userRepository.save(updateUserData);
    }

    @GetMapping("/api/v1/health")
    public ResponseEntity<Answer> health() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Answer("OK"));
    }

}


