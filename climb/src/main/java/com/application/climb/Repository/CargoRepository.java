package com.application.climb.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.application.climb.Model.Cargo;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> 
 {
    Optional<Cargo> findByNome(String nome);
}
