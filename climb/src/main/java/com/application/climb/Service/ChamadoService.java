package com.application.climb.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.climb.Dto.ChamadoDTO;
import com.application.climb.Model.Chamado;
import com.application.climb.Model.Status;
import com.application.climb.Repository.ChamadoRepository;
import com.application.climb.Repository.StatusRepository;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private StatusRepository statusRepository;


    public Chamado save(Chamado chamado) {
        
        if (chamado.getData() == null) {
            chamado.setData(LocalDateTime.now());
        }
        return chamadoRepository.save(chamado);
    }

    public Optional<Chamado> findById(Integer id) {
        return chamadoRepository.findById(id);
    }

    public List<Chamado> findAll() {
    return chamadoRepository.findAll();
    }

   public List<Chamado> listarChamadosPorSetor(Integer setorId) {
    return chamadoRepository.findAllSemConclusaoPorSetor(setorId, setorId);
    }

   public List<Chamado> listarChamadosSemConclusao() {
    return chamadoRepository.findAllSemConclusao();
    }

    public Chamado atualizarStatus(Integer id, Integer statusId, ChamadoDTO payload) {

        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));

        Status novoStatus = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Status não encontrado"));

        chamado.setStatus(novoStatus);

        // Atualizar outros campos opcionais (se vierem no body)
        if (payload.getDescricao() != null) chamado.setDescricao(payload.getDescricao());
        if (payload.getMotivo() != null) chamado.setMotivo(payload.getMotivo());

        return chamadoRepository.save(chamado);
    }

    //public List<Chamado> findBySetorPermissao(Integer setor, Integer permissao) {
       // return chamadoRepository.findBySetorIdPermissaoId(setor, permissao);
   // }


}
