package com.example.security.service;

import com.example.security.entity.Users;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    ResponseEntity<String> addUser(Users users);
    ResponseEntity<String> generateJWT(Users users, HttpServletRequest request);
}
