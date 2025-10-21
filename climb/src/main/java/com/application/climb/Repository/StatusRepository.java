package com.application.climb.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.climb.Model.Status;

@Repository
public interface StatusRepository extends 
JpaRepository<Status, Integer> {
    Optional<Status> findByNome(String nome);
    Optional<Status> findByEmpresaId(Integer empresaId);
}
