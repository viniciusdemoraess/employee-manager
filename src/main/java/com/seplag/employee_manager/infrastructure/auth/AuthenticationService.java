package com.seplag.employee_manager.infrastructure.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.seplag.employee_manager.application.exception.ResourceNotFoundException;
import com.seplag.employee_manager.application.io.LoginRequest;
import com.seplag.employee_manager.application.io.LoginResponse;
import com.seplag.employee_manager.infrastructure.pesistence.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

    
    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    public LoginResponse authenticate(final LoginRequest input) {

        final var user = this.userRepository.findByEmail(input.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // this.authenticationManager.authenticate(
        //     new UsernamePasswordAuthenticationToken(
        //         input.getEmail(),
        //         input.getPassword()
        //     )
        // );

        final String jwtToken = this.jwtService.generateToken(user);

        return LoginResponse.builder()
            .token(jwtToken)
            .expiresIn(this.jwtService.getExpirationTime())
            .build();
    }
    
}
