package com.seplag.employee_manager.infrastructure.pesistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seplag.employee_manager.domain.entity.Perfil;
import com.seplag.employee_manager.domain.enums.PerfilUsuario;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long>  {

    Optional<Perfil> findByName(PerfilUsuario name);
    
}
