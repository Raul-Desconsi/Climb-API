package com.application.climb.Service;

import com.application.climb.Model.Setor;
import com.application.climb.Repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SetorService {

    @Autowired
    private SetorRepository setorRepository;

    public List<Setor> listarTodos() {
        return setorRepository.findAll();
    }

    public List<Setor> listarPorEmpresaId(Integer empresaId) {
        return setorRepository.findByEmpresaId(empresaId);
    }


    public Optional<Setor> buscarPorId(Integer id) {
        return setorRepository.findById(id);
    }

    public Setor salvar(Setor setor) {
        return setorRepository.save(setor);
    }

    public List<Setor> buscarPorEmpresaId(Integer empresaId) {
    return setorRepository.findByEmpresaId(empresaId);
}



    public Setor atualizar(Integer id, Setor setorAtualizado) {
        Optional<Setor> setorExistente = setorRepository.findById(id);
        if (setorExistente.isPresent()) {
            Setor setor = setorExistente.get();
            setor.setNome(setorAtualizado.getNome());
            setor.setEmpresa(setorAtualizado.getEmpresa());
            setor.setFuncionarios(setorAtualizado.getFuncionarios());
            setor.setChamados(setorAtualizado.getChamados());
            return setorRepository.save(setor);
        } else {
            throw new RuntimeException("Setor com ID " + id + " não encontrado.");
        }
    }
    

    // Deletar setor por ID
    public void deletar(Integer id) {
        if (setorRepository.existsById(id)) {
            setorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Setor com ID " + id + " não encontrado.");
        }
    }
}
