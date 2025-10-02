package com.application.climb.Controller;

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

import com.application.climb.Model.Funcionario;
import com.application.climb.Service.AuthService;
import com.application.climb.Service.FuncionarioService;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

@Autowired
FuncionarioService funcionarioService;

@Autowired
AuthService authService;


//Post de formulario (formFromWeb) para criar token e verificar senha e email e nivel de permissão 

@PostMapping("/login")
public ResponseEntity<?> funcionarioLogin(@RequestBody Funcionario formFromWeb) {
    try {
        String token = authService.login(formFromWeb).get();
        Funcionario funcionario = funcionarioService.findByEmail(formFromWeb.getEmail());

        return ResponseEntity.ok(Map.of(
            "token", token,
            "nivelPermissao", funcionario.getNivelPermissao(),
            "id", funcionario.getId(),
            "setor", funcionario.getSetor().getId(),
            "empresaId",  funcionario.getEmpresa().getId(),
            "nome", funcionario.getNome(),
            "funcao", funcionario.getFuncao()
        ));

    } catch (RuntimeException e) {
        if ("Sem permissão".equals(e.getMessage())) {
            return ResponseEntity.status(403).body(e.getMessage());
        } else {
            return ResponseEntity.status(401).body("Email ou senha incorretos");
        }
    }
}


// Get funcionario pelo id + necessidade de token para validar permissão para realizar a entrega do ResponseEntity
@GetMapping("/GetFuncionario")
public ResponseEntity<?> getFuncionario(@RequestParam Long id, @RequestHeader("Authorization") String token) {

    try {
        if (!authService.authenticate(token)) {
            return ResponseEntity.status(403).body("Sem permissão");
        }

        Optional<Funcionario> funcionarioOPT = funcionarioService.findById(id);

        if (funcionarioOPT.isPresent()) {
            return ResponseEntity.ok(funcionarioOPT.get());
        } else {
            return ResponseEntity.status(404).body("Funcionário inexistente");
        }
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
    }
}


}



