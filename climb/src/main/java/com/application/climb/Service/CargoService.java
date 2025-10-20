package com.application.climb.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.climb.Model.Cargo;
import com.application.climb.Model.Funcionario;
import com.application.climb.Repository.CargoRepository;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    public Optional<Cargo> buscarPorNome(String nome) {
        return cargoRepository.findByNome(nome);
    }

    public List<Cargo> listarTodos() {
        return cargoRepository.findAll();
    }

    public List<Cargo> findCargosByEmpresaId(Long id) {
    return this.cargoRepository.findCargosByEmpresaId(id);
    }

    public Optional<Cargo> findById(Integer id) {
       return this.cargoRepository.findById(id);
}

}


