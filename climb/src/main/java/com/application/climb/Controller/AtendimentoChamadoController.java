package com.application.climb.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.climb.Dto.AtendimentoChamadoDTO;
import com.application.climb.Model.AtendimentoChamado;
import com.application.climb.Model.Chamado;
import com.application.climb.Model.Funcionario;
import com.application.climb.Model.Setor;
import com.application.climb.Service.AtendimentoChamadoService;
import com.application.climb.Service.AuthService;
import com.application.climb.Service.ChamadoService;
import com.application.climb.Service.FuncionarioService;
import com.application.climb.Service.SetorService;

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
            String rawToken = token != null ? token.replace("Bearer ", "").trim() : null;

            if (!authService.authenticate(rawToken)) {
                return ResponseEntity.status(403).body("Sem permissão");
            }

            // pega o usuário logado a partir do token (útil para preencher dados
            // automaticamente)
            Funcionario usuarioLogado = authService.getFuncionarioFromToken(rawToken);
            if (usuarioLogado == null) {
                return ResponseEntity.status(403).body("Usuário inválido no token");
            }

            if (dto.getResposta() == null || dto.getResposta().isBlank()) {
                return ResponseEntity.badRequest().body("Resposta é obrigatória");
            }

            AtendimentoChamado at = new AtendimentoChamado();
            at.setResposta(dto.getResposta());
            at.setConclusao_chamado(dto.getConclusaoChamado() != null ? dto.getConclusaoChamado() : 0);
            // usa LocalDateTime (compatível com a entidade)
            at.setData_atendimento(dto.getDataAtendimento() != null ? dto.getDataAtendimento() : LocalDateTime.now());

            // setor direcionado (obrigatório)
            if (dto.getSetorDirecionadoId() == null) {
                return ResponseEntity.badRequest().body("Setor direcionado é obrigatório");
            }
            Optional<Setor> setorDir = setorService.buscarPorId(dto.getSetorDirecionadoId());
            if (setorDir.isEmpty())
                return ResponseEntity.status(404).body("Setor direcionado não encontrado");
            at.setSetorDirecionado(setorDir.get());

            // setor atendimento -> se não vier no dto, usa setor do usuário logado
            Integer setorAtId = dto.getSetorAtendimentoId();
            if (setorAtId == null) {
                if (usuarioLogado.getSetor() == null) {
                    return ResponseEntity.badRequest().body("Setor do atendimento não informado e usuário sem setor");
                }
                setorAtId = usuarioLogado.getSetor().getId();
            }
            Optional<Setor> setorAt = setorService.buscarPorId(setorAtId);
            if (setorAt.isEmpty())
                return ResponseEntity.status(404).body("Setor de atendimento não encontrado");
            at.setSetorAtendimento(setorAt.get());

            // responsavel atendimento -> se não vier no dto, usa id do usuário logado
            Long respId = dto.getResponsavelAtendimentoId() != null ? dto.getResponsavelAtendimentoId().longValue()
                    : usuarioLogado.getId();
            Optional<Funcionario> funcOpt = funcionarioService.findById(respId);
            if (funcOpt.isEmpty())
                return ResponseEntity.status(404).body("Funcionário não encontrado");
            at.setResponsavelAtendimento(funcOpt.get());

            // chamado
            if (dto.getChamadoId() == null) {
                return ResponseEntity.badRequest().body("Chamado é obrigatório");
            }
            Optional<Chamado> chamadoOpt = chamadoService.findById(dto.getChamadoId());
            if (chamadoOpt.isEmpty())
                return ResponseEntity.status(404).body("Chamado não encontrado");
            at.setChamado(chamadoOpt.get());

            // salva atendimento
            AtendimentoChamado salvo = atendimentoService.save(at);

            // opcional: atualizar status/setor do Chamado aqui (se desejar)...
            // Exemplo simples (se quiser atualizar setor atual do chamado para
            // setorDirecionado):
            /*
             * Chamado chamado = chamadoOpt.get();
             * chamado.setSetor(setorDir.get());
             * chamadoService.save(chamado);
             */

            return ResponseEntity.ok(Map.of(
                    "message", "Atendimento criado com sucesso",
                    "id", salvo.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAtendimento(@RequestParam Integer id,
            @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            if (!authService.authenticate(token))
                return ResponseEntity.status(403).body("Sem permissão");
            Optional<AtendimentoChamado> atOpt = atendimentoService.findById(id);
            if (atOpt.isEmpty())
                return ResponseEntity.status(404).body("Atendimento não encontrado");
            return ResponseEntity.ok(atOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<AtendimentoChamado>> listarTodos(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        if (!authService.authenticate(token))
            return ResponseEntity.status(403).build();
        List<AtendimentoChamado> lista = atendimentoService.findAll();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> excluir(@RequestParam Integer id,
            @RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        if (!authService.authenticate(token))
            return ResponseEntity.status(403).body("Sem permissão");
        try {
            atendimentoService.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Atendimento excluído"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/listarPorChamado")
    public ResponseEntity<?> listarPorChamado(@RequestParam Integer chamadoId,
            @RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        if (!authService.authenticate(token))
            return ResponseEntity.status(403).body("Sem permissão");

        List<AtendimentoChamado> lista = atendimentoService.findByChamadoId(chamadoId);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/finalizar")
    public ResponseEntity<?> concluirChamado(@RequestBody AtendimentoChamadoDTO dto,
            @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            if (!authService.authenticate(token)) {
                return ResponseEntity.status(403).body("Sem permissão");
            }

            if (dto.getChamadoId() == null) {
                return ResponseEntity.badRequest().body("Chamado é obrigatório");
            }

            Optional<Chamado> chamadoOpt = chamadoService.findById(dto.getChamadoId());
            if (chamadoOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Chamado não encontrado");
            }

            AtendimentoChamado at = new AtendimentoChamado();
            at.setResposta(dto.getResposta());
            at.setConclusao_chamado(1);
            at.setData_atendimento(LocalDateTime.now());

            Optional<Setor> setorAt = setorService.buscarPorId(dto.getSetorAtendimentoId());
            if (setorAt.isEmpty())
                return ResponseEntity.status(404).body("Setor do atendimento não encontrado");
            at.setSetorAtendimento(setorAt.get());

            Optional<Setor> setorDir = setorService.buscarPorId(dto.getSetorDirecionadoId());
            at.setSetorDirecionado(setorDir.orElse(setorAt.get()));

            Optional<Funcionario> func = funcionarioService.findById(dto.getResponsavelAtendimentoId().longValue());
            if (func.isEmpty())
                return ResponseEntity.status(404).body("Funcionário não encontrado");
            at.setResponsavelAtendimento(func.get());

            at.setChamado(chamadoOpt.get());
            atendimentoService.save(at);

            Chamado chamado = chamadoOpt.get();
            chamadoService.save(chamado);

            return ResponseEntity.ok(Map.of(
                    "message", "Chamado concluído com sucesso",
                    "chamadoId", chamado.getId()));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

}
