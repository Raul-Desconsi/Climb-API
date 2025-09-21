package com.application.climb.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;

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

    private Integer id;
    private String resposta;
    private LocalDateTime data_atendimento;
    private Funcionario id_responsavel_atendimento;
    private Chamado id_chamado;
}
