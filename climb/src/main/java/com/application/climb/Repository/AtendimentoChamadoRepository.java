package com.application.climb.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.application.climb.Model.AtendimentoChamado;

@Repository
public interface AtendimentoChamadoRepository extends JpaRepository<AtendimentoChamado, Integer> {
    List<AtendimentoChamado> findByChamadoId(Integer chamadoId);

}
