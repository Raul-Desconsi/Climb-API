package com.application.climb.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.climb.Model.Chamado;
import com.application.climb.Repository.ChamadoRepository;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    public Chamado save(Chamado chamado) {
        
        if (chamado.getData() == null) {
            chamado.setData(LocalDate.now());
        }
        return chamadoRepository.save(chamado);
    }

    public Optional<Chamado> findById(Integer id) {
        return chamadoRepository.findById(id);
    }

    public List<Chamado> findAll() {
    return chamadoRepository.findAll();
    }


}
