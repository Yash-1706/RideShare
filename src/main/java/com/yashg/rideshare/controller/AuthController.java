package com.yashg.rideshare.controller;

import com.yashg.rideshare.config.JwtUtil;
import com.yashg.rideshare.dto.AuthResponse;
import com.yashg.rideshare.dto.LoginRequest;
import com.yashg.rideshare.dto.RegisterRequest;
import com.yashg.rideshare.exception.BadRequestException;
import com.yashg.rideshare.model.User;
import com.yashg.rideshare.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwt;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        userRepository.findByUsername(req.getUsername()).ifPresent(u -> {
            throw new BadRequestException("Username already exists");
        });

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(req.getRole());

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse("Registered successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        String token = jwt.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(new AuthResponse("Login successful", token));
    }
}