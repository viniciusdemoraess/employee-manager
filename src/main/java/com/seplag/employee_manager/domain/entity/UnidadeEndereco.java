package com.seplag.employee_manager.domain.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "unidade_endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeEndereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "unid_id", nullable = false)
    @JsonBackReference("unidade-endereco")
    private Unidade unidade;

    @ManyToOne
    @JoinColumn(name = "end_id", nullable = false)
    private Endereco endereco;
}

