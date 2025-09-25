package com.application.climb.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.climb.Model.Funcionario;
import com.application.climb.Repository.FuncionarioRepository;
import java.util.Optional;


@Service
public class FuncionarioService {

@Autowired
FuncionarioRepository funcionarioRepository;


public Funcionario findByEmail(String email) {

    
    return this.funcionarioRepository.findByEmail(email.toLowerCase().trim()).orElseThrow(() -> new RuntimeException("not found"));
}

public Optional<Funcionario> findById(Long id) {
    return this.funcionarioRepository.findById(id);
}


}



