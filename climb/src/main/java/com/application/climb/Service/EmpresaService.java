package com.application.climb.Service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.climb.Model.Empresa;
import com.application.climb.Repository.EmpresaRepository;

@Service
public class EmpresaService {

@Autowired
EmpresaRepository empresaRepository;

public Optional<Empresa> findById(Long id) {
    return this.empresaRepository.findById(id);
}
    

}
