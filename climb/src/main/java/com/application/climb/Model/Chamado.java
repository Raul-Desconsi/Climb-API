package com.application.climb.Model;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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
    private Integer id;
    private String motivo;
    private LocalDateTime data;
    private String areaAfetada;
    private String descricao;
    private String status;
    private Funcionario idResponsavelAbertura;
    private Funcionario idResponsavelResolucao;
    private enum urgencia{Baixa, Media, Alta};
    private Setor idSetor;
    private enum Status{Analise, Desenvolvimento, Devolvido, Concluido}

}
