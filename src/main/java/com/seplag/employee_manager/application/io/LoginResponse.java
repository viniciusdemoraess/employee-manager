package com.seplag.employee_manager.application.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @EqualsAndHashCode(callSuper = false)
public class LoginResponse {

    private String token;

    private long expiresIn;

}
