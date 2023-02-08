package com.example.security.service.Impl;

import com.example.security.entity.Users;
import com.example.security.redis.RedisRepository;
import com.example.security.redis.UsersSession;
import com.example.security.repository.UsersRepository;
import com.example.security.security.JwtUtil;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RedisRepository redisRepository;
    @Override
    public ResponseEntity<String> addUser(Users users) {
        try {
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            repository.save(users);
            return ResponseEntity.ok("Successfully saved");
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> generateJWT(Users users, HttpServletRequest request) {
        Optional<Users> optional = repository.findByUsername(users.getUsername());
        if(optional.isEmpty()) {
            return ResponseEntity.status(401).body("Error username!");
        }
        Users user = optional.get();
        if(!passwordEncoder.matches(users.getPassword(),user.getPassword())) {
            throw new BadCredentialsException("Password is incorrect!");
        }
        try {
            UsersSession usersSession = new UsersSession(sysGuid(),user);
            redisRepository.save(usersSession);
            String token = jwtUtil.generateToken(usersSession.getId());
            return ResponseEntity.status(200).body(token);
        }catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
    private String sysGuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
