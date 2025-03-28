package com.seplag.employee_manager.application.io;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class RefreshTokenRequest {
    private String refreshToken;
}