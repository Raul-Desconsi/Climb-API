package com.application.climb.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.climb.Model.Funcionario;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    @Query("SELECT u FROM Funcionario u WHERE u.email = :email")
    Optional<Funcionario> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM Funcionario u WHERE u.empresa.id = :id")
    List<Funcionario> findByEmpresaId(@Param("id") Long id);

}
