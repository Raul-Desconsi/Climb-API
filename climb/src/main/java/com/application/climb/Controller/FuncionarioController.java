package com.application.climb.Controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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




@PostMapping("/login")
public ResponseEntity<?> funcionarioLogin(@RequestBody Funcionario formFromWeb) {
    try {
        String token = authService.login(formFromWeb).get();
        Funcionario funcionario = funcionarioService.findByEmail(formFromWeb.getEmail());

        return ResponseEntity.ok(Map.of(
            "token", token,
            "nivelPermissao", funcionario.getNivelPermissao(),
            "id", funcionario.getId(),
            "setor", funcionario.getSetor().getId()
        ));

    } catch (RuntimeException e) {
        if ("Sem permissão".equals(e.getMessage())) {
            return ResponseEntity.status(403).body(e.getMessage());
        } else {
            return ResponseEntity.status(401).body("Email ou senha incorretos");
        }
    }
}














}



