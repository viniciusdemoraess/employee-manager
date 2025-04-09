package com.seplag.employee_manager.infrastructure.pesistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seplag.employee_manager.domain.entity.ServidorEfetivo;

@Repository
public interface ServidorEfetivoRepository extends JpaRepository<ServidorEfetivo, Long> { 

    Page<ServidorEfetivo> findByNomeContainingIgnoreCase(String nome, Pageable pageable);


}
