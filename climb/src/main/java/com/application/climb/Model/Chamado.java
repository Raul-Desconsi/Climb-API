package com.application.climb.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
public class Chamado {
    @Getter
    @Setter

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(length =100, nullable = false, unique = false)
    private String motivo;

    @Column(nullable = false, unique = false)
    private LocalDateTime data;

    @Column(length =60, nullable = false, unique = false)
    private String areaAfetada;

    @Column(length =255, nullable = false, unique = false)
    private String descricao;

    @Column(length =255, nullable = true, unique = false)
    private Funcionario idResponsavelAbertura;

    @Column(length =255, nullable = true, unique = false)
    private Funcionario idResponsavelResolucao;

    @Column(length =15, nullable = false, unique = false)
    private Urgencia urgencia;

    public enum Urgencia {
        Baixa,
        Media,
        Alta
    }

    @Column(nullable = false, unique = false)
    private Setor idSetor;

    @Column(length= 30, nullable = false, unique = false)
    private Status status;
    
    public enum Status{
        Analise,
        Desenvolvimento,
        Devolvido,
        Concluido
    }

}
