package com.application.climb.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

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

    @OneToMany(mappedBy = "empresa",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER )
    private List<Setor> setores;
}
