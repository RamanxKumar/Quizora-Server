package com.projects.controller;

import com.projects.dto.AuthRequest;
import com.projects.model.User;
import com.projects.repository.UserRepository;
import com.projects.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Register new user
    @PostMapping("/register")
    public String register(@RequestBody User user) {

        // ✅ Username must be unique
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already taken!";
        }

        // ✅ Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ✅ Enforce role logic
        if (user.getUsername().trim().equalsIgnoreCase("quizoraa")) {
            user.setRole("ROLE_ADMIN");
        } else {
            user.setRole("ROLE_USER");
        }

        userRepository.save(user);

        return "User registered successfully!";
    }

    // ✅ Login existing user
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        return token;
    }
}
