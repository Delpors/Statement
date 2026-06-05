package com.example.statement.controller;

import com.example.statement.dto.request.AuthRequest;
import com.example.statement.dto.request.RegisterRequest;
import com.example.statement.dto.response.AuthResponse;
import com.example.statement.entity.UserEntity;
import com.example.statement.repository.UserRepository;
import com.example.statement.sequryti.JwtService;
import com.example.statement.service.UserService;
import com.example.statement.util.UserRole;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
        authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken
                        (request.userName(), request.password()));

        UserEntity user = userService.getUserByUserName(request.userName());

        String token = jwtService.generateToken(new UserPrincipal(user));

        return ResponseEntity.ok
                (new AuthResponse(
                        token,
                        user.getUsername(),
                        user.getFullName(),
                        user.getRole().toString()
                ));
    }

    @PostMapping("/register")
    ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        boolean isFirstUser = userRepository.count() == 0;
        UserRole role = isFirstUser ? UserRole.ADMIN: UserRole.USER;

        UserEntity user = new UserEntity(
                request.userName(),
                passwordEncoder.encode(request.password()),
                request.fullName(),
                request.email(),
                role,
                null
        );

        userRepository.save(user);

        String token = jwtService.generateToken(new UserPrincipal(user));
        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        user.getUserName(),
                        user.getFullName(),
                        user.getRol().toString()
                )
        );
    }
}
