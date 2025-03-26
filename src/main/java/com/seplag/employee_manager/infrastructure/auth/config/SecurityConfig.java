package com.seplag.employee_manager.infrastructure.auth.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.seplag.employee_manager.application.filter.JwtAuthenticationFilter;

import java.util.List;

// import br.gov.mt.fapemat.xsig.application.filter.ApiKeyAuthenticationFilter;
// import br.gov.mt.fapemat.xsig.application.filter.ApiKeyUtils;
// import br.gov.mt.fapemat.xsig.application.filter.JwtAuthenticationFilter;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] AUTHENTICATION_NOT_REQUIRED = {
        "/auth/**",
        "/swagger-ui/**",
        "/webjars/**",
        "/swagger-resources/**",
        "/swagger-ui/index.html",
        "/api-docs/**",
        "/actuator/**",
        "/instances/**",
        "/wallboard/**",
        "/applications/**",
        "/api/servidores**"
    };

    // private static final String[] PUBLIC_USER_ENDPOINTS = {
    //     "/users/me"
    // };

    private static final String[] ADMIN_USER_ENDPOINTS = {
        "/users/**",
        "/perfis/**"
    };

    // private static final String[] SIGFAPEMAT_ADMIN_USER_ENDPOINTS = {
    //     "/automations/**",
    //     "/bolsistas/**",
    //     "/editais/**",
    //     "/foos/**"
    // };

    // private static final String[] SIGADOC_ADMIN_USER_ENDPOINTS = {
    //     "/processos/**",
    //     "/bolsistas/*/documents/*",
    //     "editais/*/oficio/**"
    // };

    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final ObjectMapper objectMapper;

    public SecurityConfig(
        final JwtAuthenticationFilter jwtAuthenticationFilter,
        final AuthenticationProvider authenticationProvider,
        final ObjectMapper objectMapper
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(
                authorize ->
                    authorize.requestMatchers(AUTHENTICATION_NOT_REQUIRED).permitAll()
                        .requestMatchers(ADMIN_USER_ENDPOINTS).hasAuthority("ADMINISTRADOR")
                        .anyRequest()
                        .authenticated()
            ).sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(this.authenticationProvider)
            .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
            "http://localhost:8080",
            "http://localhost:4200"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
