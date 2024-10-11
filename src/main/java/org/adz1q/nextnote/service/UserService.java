package org.adz1q.nextnote.service;

import org.adz1q.nextnote.model.User;
import org.adz1q.nextnote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final MessageDigest messageDigest;

    @Autowired
    public UserService(UserRepository userRepository) throws NoSuchAlgorithmException {
        this.userRepository = userRepository;
        this.messageDigest = MessageDigest.getInstance("SHA-256");
    }

    private String hashPassword(String password) {
        byte[] hashedBytes = messageDigest.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public ResponseEntity<Object> createUser(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<Object> getUser(int id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        return ResponseEntity.ok(user);
    }
}
