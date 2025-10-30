package com.application.climb.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.climb.Dto.ChamadoDTO;
import com.application.climb.Model.Chamado;
import com.application.climb.Model.Funcionario;
import com.application.climb.Model.Setor;
import com.application.climb.Model.Status;
import com.application.climb.Model.Urgencia;
import com.application.climb.Service.AuthService;
import com.application.climb.Service.ChamadoService;
import com.application.climb.Service.FuncionarioService;
import com.application.climb.Service.SetorService;
import com.application.climb.Service.StatusService;
import com.application.climb.Service.UrgenciaService;

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
            chamado.setData(dto.getData() != null ? dto.getData() : LocalDate.now());

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
                Optional<Status> statusPadrao = statusService.buscarPorNome("Na recepção");
                if (statusPadrao.isPresent()) {
                    chamado.setStatus(statusPadrao.get());
                } else {
                    return ResponseEntity.status(404).body("Status não encontrado no banco");
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
    public ResponseEntity<List<ChamadoDTO>> listarTodos(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String urgencia,
            @RequestParam(required = false) String responsavel
    ) {
        if (!authService.authenticate(token.replace("Bearer ", ""))) {
            return ResponseEntity.status(403).build();
        }

        List<Chamado> chamados = chamadoService.findAll();

        // 🔍 Filtros simples no Java (pode mover para Query mais tarde)
        List<ChamadoDTO> filtrados = chamados.stream()
                .filter(c -> status == null || 
                        (c.getStatus() != null && c.getStatus().getNome().equalsIgnoreCase(status)))
                .filter(c -> urgencia == null || 
                        (c.getUrgencia() != null && c.getUrgencia().getNome().equalsIgnoreCase(urgencia)))
                .filter(c -> responsavel == null || 
                        (c.getResponsavelAbertura() != null && c.getResponsavelAbertura().getNome().equalsIgnoreCase(responsavel)))
                .map(ChamadoDTO::new)
                .toList();

        return ResponseEntity.ok(filtrados);
    }
}
