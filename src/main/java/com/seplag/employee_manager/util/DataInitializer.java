package com.seplag.employee_manager.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.seplag.employee_manager.domain.entity.Perfil;
import com.seplag.employee_manager.domain.entity.User;
import com.seplag.employee_manager.domain.enums.PerfilUsuario;
import com.seplag.employee_manager.infrastructure.pesistence.PerfilRepository;
import com.seplag.employee_manager.infrastructure.pesistence.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner  {

    private final UserRepository userRepository;

    private final PerfilRepository perfilRepository;

    @Override
    public void run(String... args) {

        if (!perfilRepository.existsById(1L)) {
            Perfil perfil = new Perfil();
            perfil.setName(PerfilUsuario.ADM);
            perfilRepository.save(perfil);
        }

        if (!userRepository.existsByEmail("admin@admin.com")) {
            Perfil perfil = perfilRepository.findByName(PerfilUsuario.ADM).orElseThrow();

            User user = new User();
            user.setName("admin");
            user.setEmail("admin@admin.com");
            user.setPassword("$2a$10$KyZ4xi.a5UwK.BhfutUT8OW4xZNq00HlGNw1Z0FZ6ta7ZM5vjkNWW");
            user.setEnabled(true);
            user.setPerfil(perfil);
            userRepository.save(user);
        }
    }
}

