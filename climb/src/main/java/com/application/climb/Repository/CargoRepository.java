package com.application.climb.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.application.climb.Model.Cargo;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> 
 {
    Optional<Cargo> findByNome(String nome);

    @Query("SELECT u FROM Cargo u WHERE u.empresa.id = :id")
    List<Cargo> findCargosByEmpresaId(@Param("id") Long id);
}
