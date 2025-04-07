package com.seplag.employee_manager.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "servidor_efetivo")
public class ServidorEfetivo extends Pessoa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "se_matricula", length = 20, nullable = false, unique = true)
    private String matricula;
}
