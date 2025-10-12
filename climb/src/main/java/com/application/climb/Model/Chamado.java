package com.application.climb.Model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chamado")
@Getter
@Setter
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(length = 255, nullable = false)
    private String descricao;

    @ManyToOne
    private Setor areaAfetada;

    @ManyToOne
    private Funcionario responsavelAbertura;

    @ManyToOne
    private Funcionario responsavelResolucao;

    @ManyToOne
    private Urgencia urgencia;

    @ManyToOne
    private Setor setor;

    @ManyToOne
    private Status status;

    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AtendimentoChamado> atendimentosChamado;

    
}
