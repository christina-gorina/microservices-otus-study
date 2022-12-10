package com.christinagorina.controller;

import com.christinagorina.model.User;
import com.christinagorina.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<String> indexPage() {
        System.out.println("method /");
        return new ResponseEntity<>("Users service main page success", HttpStatus.OK);
    }

    /*@GetMapping("/api/v1/users")
    public List<User> findByName(){
        System.out.println("userRepository.findAll");
        return userRepository.findAll();
    }*/

    @GetMapping("/api/v1/user/name/{name}")
    public User findByName(@PathVariable("name") String name, @RequestHeader("x-auth-user") String xAuthUser) throws IllegalAccessException {
        System.out.println("findByName name = " + name);
        System.out.println("findByName xAuthUser = " + xAuthUser);
        Optional.ofNullable(xAuthUser).filter(n -> n.equals(name)).orElseThrow(IllegalAccessException::new);
        System.out.println("findByName after check");
        return userRepository.findByName(name);
    }

    @PostMapping("/api/v1/user")
    public User create(@RequestBody User user) {
        System.out.println("findByName user = " + user);
        Optional.ofNullable(user).filter(u -> Objects.isNull(u.getId())).orElseThrow(IllegalArgumentException::new);
        System.out.println("create after check");
        return userRepository.save(user);
    }

    @PutMapping("/api/v1/user/name/{name}")
    public User update(@RequestBody User updateUserData, @PathVariable String name, @RequestHeader("x-auth-user") String xAuthUser) throws IllegalAccessException {
        System.out.println("update updateUserData = " + updateUserData);
        System.out.println("update name = " + name);
        System.out.println("update xAuthUser = " + xAuthUser);
        Optional.ofNullable(xAuthUser).filter(n -> n.equals(name)).orElseThrow(IllegalAccessException::new);
        System.out.println("update after check1");
        User existingUser = Optional.of(userRepository.findByName(name)).orElseThrow(IllegalArgumentException::new);
        System.out.println("update after check2");
        if(Objects.isNull(updateUserData.getPassword())){
            System.out.println("update getPassword = " + updateUserData.getPassword());
            updateUserData.setPassword(existingUser.getPassword());
        }

        if(Objects.isNull(updateUserData.getName())){
            System.out.println("update getName = " + updateUserData.getName());
            updateUserData.setName(existingUser.getName());
        }
        System.out.println("update getId = " + existingUser.getId());
        updateUserData.setId(existingUser.getId());
        System.out.println("update updateUserData = " + updateUserData);
        return userRepository.save(updateUserData);
    }

   /* @DeleteMapping("/api/v1/user/{name}")
    public String deleteByName(@PathVariable("name") String name) {
        System.out.println("deleteByName name = " + name);
        userRepository.deleteById(Optional.ofNullable(userRepository.findByName(name)).map(User::getId).orElseThrow(IllegalArgumentException::new));
        System.out.println("deleteByName deleted");
        return "User with name " + name + " deleted";
    }*/

}


