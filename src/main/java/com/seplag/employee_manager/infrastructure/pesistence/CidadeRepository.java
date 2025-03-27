package com.seplag.employee_manager.infrastructure.pesistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seplag.employee_manager.domain.entity.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> { }