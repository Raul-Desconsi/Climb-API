package com.application.climb.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.climb.Model.Urgencia;
import com.application.climb.Repository.UrgenciaRepository;

@Service
public class UrgenciaService {

    @Autowired
    private UrgenciaRepository urgenciaRepository;

    public List<Urgencia> listarTodas() {
        return urgenciaRepository.findAll();
    }

    public List<Urgencia> listarPorEmpresa(Integer empresaId) {
        return urgenciaRepository.findByEmpresaId(empresaId);
    }

    public Optional<Urgencia> buscarPorId(Integer id) {
        return urgenciaRepository.findById(id);
    }

    public Urgencia buscarPorNome(String nome) {
        return urgenciaRepository.findByNome(nome);
    }

    public Urgencia salvar(Urgencia urgencia) {
        return urgenciaRepository.save(urgencia);
    }

    public void deletar(Integer id) {
        urgenciaRepository.deleteById(id);
    }
}
