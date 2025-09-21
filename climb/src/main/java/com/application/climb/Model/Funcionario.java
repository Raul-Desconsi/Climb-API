package com.application.climb.Model;
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


public class Funcionario {
    @Getter
    @Setter

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true) 
    private Integer id;

    
    @Column(length =14, nullable = false, unique = true)
    private String cpf;


    @Column(length =70, nullable = false, unique = false)
    private String nome;


    @Column(length =100, nullable = false, unique = true)
    private String email;

    
    @Column(length =255, nullable = false, unique = false)
    private String senha;


    @Column(nullable = false, unique = false)
    private int nivelPermissao;

    
    @Column(length =100, nullable = false, unique = false)
    private String funcao;


    private Setor idSetor;

    private Empresa idEmpresa;

    }