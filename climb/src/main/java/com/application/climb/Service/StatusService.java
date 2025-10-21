package com.application.climb.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.climb.Model.Status;
import com.application.climb.Repository.StatusRepository;

@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;

    public Optional<Status> buscarPorId(Integer id) {
        return statusRepository.findById(id);
    }

    public Optional<Status> buscarPorNome(String nome) {
        return statusRepository.findByNome(nome);
    }

    public Optional<Status> buscarPorEmpresaId(Integer empresaId) {
        return statusRepository.findByEmpresaId(empresaId);
    }

    public List<Status> findAll() {
        return statusRepository.findAll();
    }
}
