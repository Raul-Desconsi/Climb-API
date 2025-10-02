package com.application.climb.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "empresa")
@Getter
@Setter
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 18, nullable = false, unique = true, updatable = false)
    private String cnpj; 

    @Column(length = 90, nullable = false)
    private String nome;

    @Column(length = 9, nullable = false)
    private String cep;

    @Column(length = 100, nullable = false, unique = true, updatable = false)
    private String email;

    @Column(length = 20)
    private String telefone;
    
    @JsonIgnore
    @OneToMany(mappedBy = "empresa",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER )
    
    private List<Setor> setores;
}
