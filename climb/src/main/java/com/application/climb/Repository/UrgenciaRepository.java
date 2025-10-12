package com.application.climb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.application.climb.Model.Urgencia;

@Repository
public interface UrgenciaRepository extends JpaRepository<Urgencia, Integer> {
    Urgencia findByNome(String nome);
}
