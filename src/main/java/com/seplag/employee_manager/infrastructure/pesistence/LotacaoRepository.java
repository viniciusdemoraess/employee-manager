package com.seplag.employee_manager.infrastructure.pesistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.seplag.employee_manager.domain.entity.Lotacao;
import com.seplag.employee_manager.domain.entity.Pessoa;
import com.seplag.employee_manager.domain.entity.ServidorEfetivo;

@Repository
public interface LotacaoRepository extends JpaRepository<Lotacao, Long> {

    List<Lotacao> findByPessoa(Pessoa pessoa);

    @Query("""
        SELECT l.pessoa
        FROM Lotacao l
        WHERE l.unidade.id = :unidId
          AND TYPE(l.pessoa) = ServidorEfetivo
    """)
    Page<ServidorEfetivo> findServidoresEfetivosPorUnidade(@Param("unidId") Long unidId, Pageable pageable);

    

 }