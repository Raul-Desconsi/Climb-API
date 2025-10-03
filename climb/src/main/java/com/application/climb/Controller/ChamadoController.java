package com.application.climb.Controller;

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
import com.application.climb.Model.Chamado.Status;
import com.application.climb.Model.Chamado.Urgencia;
import com.application.climb.Model.Funcionario;
import com.application.climb.Service.AuthService;
import com.application.climb.Service.ChamadoService;
import com.application.climb.Service.FuncionarioService;
import com.application.climb.Model.Setor;
import com.application.climb.Service.SetorService; // se não existir, adaptar

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

    @PostMapping("/create")
    public ResponseEntity<?> criarChamado(@RequestBody ChamadoDTO dto,
                                         @RequestHeader("Authorization") String token) {
        try {
            if (!authService.authenticate(token)) {
                return ResponseEntity.status(403).body("Sem permissão");
            }

            Chamado chamado = new Chamado();

            chamado.setMotivo(dto.getMotivo());
            chamado.setAreaAfetada(dto.getAreaAfetada());
            chamado.setDescricao(dto.getDescricao());

            String urg = dto.getUrgencia();
            if (urg != null) {
                switch (urg.toLowerCase()) {
                    case "baixa":
                        chamado.setUrgencia(Urgencia.Baixa);
                        break;
                    case "media":
                        chamado.setUrgencia(Urgencia.Media);
                        break;
                    case "alta":
                        chamado.setUrgencia(Urgencia.Alta);
                        break;
                    default:
                        chamado.setUrgencia(Urgencia.Baixa);
                        break;
                }
            } else {
                chamado.setUrgencia(Urgencia.Baixa);
            }

            chamado.setStatus(Status.Analise);

            if (dto.getResponsavelAberturaId() != null) {
                Optional<Funcionario> fopt = funcionarioService.findById(dto.getResponsavelAberturaId().longValue());
                if (fopt.isPresent()) {
                    chamado.setResponsavelAbertura(fopt.get());
                } else {
                    return ResponseEntity.status(404).body("Funcionário (responsavelAbertura) não encontrado");
                }
            } else {
                return ResponseEntity.status(400).body("Responsável pela abertura não informado");
            }

            // setor: se informado, busca setor
            if (dto.getSetorId() != null && setorService != null) {
                Optional<Setor> sopt = setorService.buscarPorId(dto.getSetorId());
               if (sopt.isPresent()) {
                    chamado.setSetor(sopt.get());
                } else {
                    return ResponseEntity.status(404).body("Setor não encontrado");
                }
            }

            Chamado salvo = chamadoService.save(chamado);

            return ResponseEntity.ok(Map.of("message", "Chamado criado", "id", salvo.getId()));

        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }};

    @GetMapping("/get")
    public ResponseEntity<?> getChamado(@RequestParam Integer id, @RequestHeader("Authorization") String token) {
        try {
            if (!authService.authenticate(token)) {
                return ResponseEntity.status(403).body("Sem permissão");
            }
            Optional<Chamado> copt = chamadoService.findById(id);
            if (copt.isPresent()) {
                return ResponseEntity.ok(copt.get());
            } else {
                return ResponseEntity.status(404).body("Chamado inexistente");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    // 🔹 View com Thymeleaf (caso use templates
    // 🔹 API REST para frontend consumir JSON
    @GetMapping("/all")
    public ResponseEntity<List<Chamado>> listarTodos(@RequestHeader("Authorization") String token) {
    if (!authService.authenticate(token.replace("Bearer ", ""))) {
        return ResponseEntity.status(403).build();
    }
    List<Chamado> chamados = chamadoService.findAll();
    return ResponseEntity.ok(chamados);
}


}
