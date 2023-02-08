package com.example.security.controllers;

import com.example.security.entity.Users;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    @PostMapping("/add-user")
    public ResponseEntity<String> addUser(@RequestBody Users users) {
        return service.addUser(users);
    }
    @PostMapping("/token")
    public ResponseEntity<String> generateJWT(@RequestBody Users users, HttpServletRequest request) {
        return service.generateJWT(users,request);
    }
}
