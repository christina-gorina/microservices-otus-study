package com.christinagorina.controller;

import com.christinagorina.model.Answer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class StudentController {

    @GetMapping("/health")
    public ResponseEntity<Answer> health() {
        log.info("Start health check");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Answer("OK"));
    }

}
