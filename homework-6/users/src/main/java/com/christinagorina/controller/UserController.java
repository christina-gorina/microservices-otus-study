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

    @PostMapping("/api/v1/user")
    public User create(@RequestBody User user) {
        Optional.ofNullable(user).filter(u -> Objects.isNull(u.getId())).orElseThrow(IllegalArgumentException::new);
        return userRepository.save(user);
    }

    @GetMapping("/api/v1/user/{id}")
    public User findById(@PathVariable("id") Long id) {
        return Optional.of(userRepository.findById(id)).map(Optional::get).orElse(null);
    }

    @PutMapping("/api/v1/user/{id}")
    public User update(@RequestBody User user, @PathVariable Long id) {
        user.setId(id);
        Optional.of(userRepository.findById(user.getId())).map(Optional::get).orElseThrow(IllegalArgumentException::new);
        return userRepository.save(user);
    }

    @DeleteMapping("/api/v1/user/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "success";
    }

    @GetMapping("/api/v1/health")
    public ResponseEntity<Answer> health() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Answer("OK"));
    }

}


