package com.application.climb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.application.climb.Model.Chamado;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {
}
