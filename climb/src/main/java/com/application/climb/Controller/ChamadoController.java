package com.application.climb.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.application.climb.Dto.ChamadoDTO;
import com.application.climb.Model.*;
import com.application.climb.Service.*;

@RestController
@RequestMapping("/chamado")
public class ChamadoController {

    @Autowired
    private ChamadoService chamadoService;

    @Autowired
    private AuthService authService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired(required = false)
    private SetorService setorService;

    @Autowired(required = false)
    private UrgenciaService urgenciaService;

    @Autowired(required = false)
    private StatusService statusService; 

    @PostMapping("/create")
    public ResponseEntity<?> criarChamado(@RequestBody ChamadoDTO dto,
                                          @RequestHeader("Authorization") String token) {
        try {
            if (!authService.authenticate(token)) {
                return ResponseEntity.status(403).body("Sem permissão");
            }

            Chamado chamado = new Chamado();

            // Campos básicos
            chamado.setMotivo(dto.getMotivo());
            chamado.setDescricao(dto.getDescricao());
            chamado.setData(dto.getData() != null ? dto.getData() : LocalDateTime.now());

            // 🔹 Setor
            if (dto.getSetorId() != null && setorService != null) {
                Optional<Setor> setorOpt = setorService.buscarPorId(dto.getSetorId());
                if (setorOpt.isEmpty()) {
                    return ResponseEntity.status(404).body("Setor não encontrado");
                }
                chamado.setSetor(setorOpt.get());
            }

            // 🔹 Área afetada
            if (dto.getAreaAfetadaId() != null && setorService != null) {
                Optional<Setor> areaOpt = setorService.buscarPorId(dto.getAreaAfetadaId());
                if (areaOpt.isEmpty()) {
                    return ResponseEntity.status(404).body("Área afetada não encontrada");
                }
                chamado.setAreaAfetada(areaOpt.get());
            }

            // 🔹 Urgência
            if (dto.getUrgenciaId() != null && urgenciaService != null) {
                Optional<Urgencia> urgOpt = urgenciaService.buscarPorId(dto.getUrgenciaId());
                if (urgOpt.isEmpty()) {
                    return ResponseEntity.status(404).body("Urgência não encontrada");
                }
                chamado.setUrgencia(urgOpt.get());
            }

            // 🔹 Responsável pela abertura
            if (dto.getResponsavelAberturaId() != null) {
                Optional<Funcionario> funcOpt = funcionarioService.findById(dto.getResponsavelAberturaId().longValue());
                if (funcOpt.isEmpty()) {
                    return ResponseEntity.status(404).body("Funcionário responsável pela abertura não encontrado");
                }
                chamado.setResponsavelAbertura(funcOpt.get());
            } else {
                return ResponseEntity.status(400).body("Responsável pela abertura é obrigatório");
            }

            // 🔹 Responsável pela resolução (opcional)
            if (dto.getResponsavelResolucaoId() != null) {
                Optional<Funcionario> funcResOpt = funcionarioService.findById(dto.getResponsavelResolucaoId().longValue());
                funcResOpt.ifPresent(chamado::setResponsavelResolucao);
            }

            // 🔹 Status
            if (dto.getStatusId() != null && statusService != null) {
                Optional<Status> statusOpt = statusService.buscarPorId(dto.getStatusId());
                if (statusOpt.isEmpty()) {
                    return ResponseEntity.status(404).body("Status não encontrado");
                }
                chamado.setStatus(statusOpt.get());
            } else {
                Optional<Status> statusPadrao = statusService.buscarPorNome("Recepção");
                if (statusPadrao.isPresent()) {
                    chamado.setStatus(statusPadrao.get());
                } else {
                    return ResponseEntity.status(404).body("Status 'Análise' não encontrado no banco");
                }

            }

            Chamado salvo = chamadoService.save(chamado);

            return ResponseEntity.ok(Map.of(
                    "message", "Chamado criado com sucesso",
                    "id", salvo.getId()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getChamado(@RequestParam Integer id,
                                        @RequestHeader("Authorization") String token) {
        try {
            if (!authService.authenticate(token)) {
                return ResponseEntity.status(403).body("Sem permissão");
            }

            Optional<Chamado> chamadoOpt = chamadoService.findById(id);
            if (chamadoOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Chamado não encontrado");
            }

            return ResponseEntity.ok(chamadoOpt.get());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Chamado>> listarTodos(@RequestHeader("Authorization") String token) {
        if (!authService.authenticate(token.replace("Bearer ", ""))) {
            return ResponseEntity.status(403).build();
        }
        List<Chamado> chamados = chamadoService.findAll();
        return ResponseEntity.ok(chamados);
    }
}
