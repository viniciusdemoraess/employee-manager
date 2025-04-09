package com.seplag.employee_manager.infrastructure.auth;

import org.springframework.stereotype.Service;

import com.seplag.employee_manager.application.exception.InvalidTokenException;
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
    

    public LoginResponse authenticate(final LoginRequest input) {

        final var user = this.userRepository.findByEmail(input.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        final String jwtToken = this.jwtService.generateToken(user);

        final String refreshToken = this.jwtService.generateRefreshToken(user);


        return LoginResponse.builder()
            .token(jwtToken)
            .refreshToken(refreshToken)
            .expiresIn(this.jwtService.getExpirationTime())
            .build();
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new InvalidTokenException("Refresh token inválido ou expirado.");
        }

        String type = jwtService.extractTokenType(refreshToken);
        if (!"refresh".equals(type)) {
            throw new InvalidTokenException("O token fornecido não é um refresh token.");
        }

        String email = jwtService.extractUsername(refreshToken);
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        String newAccessToken = jwtService.generateToken(user);

        return LoginResponse.builder()
            .token(newAccessToken)
            .expiresIn(jwtService.getExpirationTime())
            .build();
    }
    
}
