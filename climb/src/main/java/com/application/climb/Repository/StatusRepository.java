package com.application.climb.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.application.climb.Model.Status;

@Repository
public interface StatusRepository extends 
JpaRepository<Status, Integer> {
    Optional<Status> findByNome(String nome);
    List<Status> findByEmpresaId(Integer empresaId);
}
