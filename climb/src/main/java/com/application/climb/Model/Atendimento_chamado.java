package com.application.climb.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
public class Atendimento_chamado {
    @Getter
    @Setter

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(length =255, nullable = false, unique = false)
    private String resposta;

    @Column(nullable = false, unique = false)
    private LocalDateTime data_atendimento;

    @Column(nullable = false, unique = false)
    private Funcionario id_responsavel_atendimento;

    @Column(nullable = false, unique = false)
    private Chamado id_chamado;
}
