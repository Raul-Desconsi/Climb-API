package com.application.climb.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.application.climb.Dto.AtendimentoChamadoDTO;
import com.application.climb.Model.*;
import com.application.climb.Service.*;

@RestController
@RequestMapping("/atendimento")
public class AtendimentoChamadoController {

    @Autowired
    private AtendimentoChamadoService atendimentoService;

    @Autowired
    private AuthService authService;

    @Autowired
    private SetorService setorService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private ChamadoService chamadoService;

    @PostMapping("/create")
    public ResponseEntity<?> criarAtendimento(@RequestBody AtendimentoChamadoDTO dto,
                                              @RequestHeader("Authorization") String token) {
        try {
            if (!authService.authenticate(token)) {
                return ResponseEntity.status(403).body("Sem permissão");
            }

            // validações básicas
            if (dto.getResposta() == null || dto.getResposta().isBlank()) {
                return ResponseEntity.badRequest().body("Resposta é obrigatória");
            }
            AtendimentoChamado at = new AtendimentoChamado();
            at.setResposta(dto.getResposta());
            at.setConclusao_chamado(dto.getConclusaoChamado() != null ? dto.getConclusaoChamado() : 0);
            at.setData_atendimento(dto.getDataAtendimento() != null ? dto.getDataAtendimento() : LocalDateTime.now());

            // setor direcionado (obrigatório)
            if (dto.getSetorDirecionadoId() == null) {
                return ResponseEntity.badRequest().body("Setor direcionado é obrigatório");
            }
            Optional<Setor> setorDir = setorService.buscarPorId(dto.getSetorDirecionadoId());
            if (setorDir.isEmpty()) return ResponseEntity.status(404).body("Setor direcionado não encontrado");
            at.setSetorDirecionado(setorDir.get());

            // setor atendimento (obrigatório)
            if (dto.getSetorAtendimentoId() == null) {
                return ResponseEntity.badRequest().body("Setor de atendimento é obrigatório");
            }
            Optional<Setor> setorAt = setorService.buscarPorId(dto.getSetorAtendimentoId());
            if (setorAt.isEmpty()) return ResponseEntity.status(404).body("Setor de atendimento não encontrado");
            at.setSetorAtendimento(setorAt.get());

            // responsavel atendimento (obrigatório)
            if (dto.getResponsavelAtendimentoId() == null) {
                return ResponseEntity.badRequest().body("Responsável pelo atendimento é obrigatório");
            }
            Optional<Funcionario> funcOpt = funcionarioService.findById(dto.getResponsavelAtendimentoId().longValue());
            if (funcOpt.isEmpty()) return ResponseEntity.status(404).body("Funcionário não encontrado");
            at.setResponsavelAtendimento(funcOpt.get());

            // chamado (obrigatório)
            if (dto.getChamadoId() == null) {
                return ResponseEntity.badRequest().body("Chamado é obrigatório");
            }
            Optional<Chamado> chamadoOpt = chamadoService.findById(dto.getChamadoId());
            if (chamadoOpt.isEmpty()) return ResponseEntity.status(404).body("Chamado não encontrado");
            at.setChamado(chamadoOpt.get());

            AtendimentoChamado salvo = atendimentoService.save(at);

            return ResponseEntity.ok(Map.of(
                "message", "Atendimento criado com sucesso",
                "id", salvo.getId()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAtendimento(@RequestParam Integer id,
                                            @RequestHeader("Authorization") String token) {
        try {
            if (!authService.authenticate(token)) return ResponseEntity.status(403).body("Sem permissão");
            Optional<AtendimentoChamado> atOpt = atendimentoService.findById(id);
            if (atOpt.isEmpty()) return ResponseEntity.status(404).body("Atendimento não encontrado");
            return ResponseEntity.ok(atOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<AtendimentoChamado>> listarTodos(@RequestHeader("Authorization") String token) {
        if (!authService.authenticate(token)) return ResponseEntity.status(403).build();
        List<AtendimentoChamado> lista = atendimentoService.findAll();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> excluir(@RequestParam Integer id,
                                     @RequestHeader("Authorization") String token) {
        if (!authService.authenticate(token)) return ResponseEntity.status(403).body("Sem permissão");
        try {
            atendimentoService.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Atendimento excluído"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }
}
