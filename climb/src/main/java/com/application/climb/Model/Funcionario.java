package com.application.climb.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "funcionario")
@Getter
@Setter
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    @Column(length = 70, nullable = false)
    private String nome;

    @Column(length = 100, nullable = false, unique = true, updatable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private String senha;

    @Column(nullable = false)
    private int nivelPermissao;

    @Column(length = 100, nullable = false)
    private String funcao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "setor_id", nullable = false)
    private Setor setor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_id", nullable = false)
    
    private Empresa empresa;
}
