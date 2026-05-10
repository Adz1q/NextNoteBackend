package org.adz1q.nextnote.service;

import lombok.Data;
import org.adz1q.nextnote.model.User;
import org.adz1q.nextnote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Object> getUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        User user = optionalUser.get();
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<Object> register(User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists!");
        }

        if (user.getPassword().length() < 8 || user.getPassword().length() > 30) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be between 8 and 30 characters!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<Object> login(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials!");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials!");
        }

        String jwtToken = jwtService.generateToken(loginRequest.getUsername());

        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }

    @Data
    public static class JwtResponse {
        private String token;

        public JwtResponse(String token) {
            this.token = token;
        }
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}