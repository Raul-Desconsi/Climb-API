package com.application.climb.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.application.climb.Model.Setor;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Integer> {
    List<Setor> findByEmpresaId(Integer empresaId);


}
