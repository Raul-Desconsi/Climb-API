package com.application.climb.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Setter;
import lombok.Getter;

@Entity
@Table
public class ChamadoSetor {
    @Getter
    @Setter

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Chamado idChamado;
    private Setor idSetor;
}

