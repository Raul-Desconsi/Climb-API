package com.application.climb.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.climb.Model.AtendimentoChamado;
import com.application.climb.Repository.AtendimentoChamadoRepository;

@Service
public class AtendimentoChamadoService {

    @Autowired
    private AtendimentoChamadoRepository repo;

    public AtendimentoChamado save(AtendimentoChamado at) {
        if (at.getData_atendimento() == null) {
            at.setData_atendimento(LocalDateTime.now());
        }
        return repo.save(at);
    }

    public Optional<AtendimentoChamado> findById(Integer id) {
        return repo.findById(id);
    }

    public List<AtendimentoChamado> findAll() {
        return repo.findAll();
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    public List<AtendimentoChamado> findByChamadoId(Integer chamadoId) {
         return repo.findByChamadoId(chamadoId);
    }
}
