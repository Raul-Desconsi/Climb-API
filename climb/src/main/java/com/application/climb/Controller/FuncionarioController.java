package com.application.climb.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.climb.Dto.FuncionarioDTO;
import com.application.climb.Model.Cargo;
import com.application.climb.Model.Funcionario;
import com.application.climb.Model.Setor;
import com.application.climb.Service.AuthService;
import com.application.climb.Service.CargoService;
import com.application.climb.Service.FuncionarioService;
import com.application.climb.Service.SetorService;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

@Autowired
FuncionarioService funcionarioService;

@Autowired
SetorService setorService;

@Autowired
AuthService authService;

@Autowired
CargoService cargoService;



//Post de formulario (formFromWeb) para criar token e verificar senha e email e nivel de permissão 

@PostMapping("/login")
public ResponseEntity<?> funcionarioLogin(@RequestBody Funcionario formFromWeb) {
    try {
        String token = authService.login(formFromWeb).get();
        Funcionario funcionario = funcionarioService.findByEmail(formFromWeb.getEmail().toLowerCase());

        return ResponseEntity.ok(Map.of(
            "token", token,
            "nivelPermissao", funcionario.getNivelPermissao(),
            "id", funcionario.getId(),
            "setor", funcionario.getSetor().getId(),
            "empresaId",  funcionario.getEmpresa().getId(),
            "nome", funcionario.getNome(),
            "cargo", funcionario.getCargo().getNome()
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
         if (authService.getFuncionarioFromToken(token).getId().intValue() != id) {
            return ResponseEntity.status(403).body("Sem permissão");
        }


        Optional<Funcionario> funcionarioOPT = funcionarioService.findById(id);

        if (funcionarioOPT.isPresent()) {

        Optional<FuncionarioDTO.Response> funcionarioDTO = funcionarioOPT.map(f -> new FuncionarioDTO.Response(
                f.getCpf(),
                f.getNome(),
                f.getEmail(),
                f.getNivelPermissao(),
                f.getCargo().getNome(),
                f.getSetor().getNome(),
                f.getEmpresa().getNome()
        ));

            return ResponseEntity.ok(funcionarioDTO.get());

        } else {
            return ResponseEntity.status(404).body("Funcionário inexistente");
        }
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
    }
}


@PostMapping("/Create")
public ResponseEntity<?> createFuncionario(@RequestHeader("Authorization") String token, @RequestBody FuncionarioDTO.Create funcionarioForm){

        // Pega o funcionario baseado no token     
        Funcionario funcionarioformtoken = authService.getFuncionarioFromToken(token);
        
    try {
        if (!authService.authenticate(token)) {
            return ResponseEntity.status(401).body("Token inválido");
        }
    if (funcionarioformtoken.getNivelPermissao() != 1) {
            return ResponseEntity.status(403).body("Sem permissão");
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setCpf(funcionarioForm.getCpf());
        funcionario.setEmail(funcionarioForm.getEmail().toLowerCase());
        funcionario.setNivelPermissao(funcionarioForm.getNivelPermissao());
        funcionario.setNome(funcionarioForm.getNome());
        Optional<Cargo> cargoOpt = cargoService.buscarPorNome(funcionarioForm.getCargo());
            if (cargoOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Cargo não encontrado");
            }
            funcionario.setCargo(cargoOpt.get());

        funcionario.setSenha(funcionarioForm.getSenha());
        
        // Criar o funcionário somente na empresa do admin
        funcionario.setEmpresa(funcionarioformtoken.getEmpresa());
        
        // Pesquisa se o setor existe
        Optional<Setor> setorFromFuncionarioFormDTO = this.setorService.buscarPorId(funcionarioForm.getSetor());

        if (setorFromFuncionarioFormDTO.isPresent()) {
                Setor setorFromFuncionarioForm = setorFromFuncionarioFormDTO.get();
                funcionario.setSetor(setorFromFuncionarioForm);
       }else{
            return ResponseEntity.status(404).body("Setor não encontrado");
       }

        this.funcionarioService.create(funcionario);
        return ResponseEntity.status(201).body("funcionário criado");
        
    } catch (Exception e) {
            return ResponseEntity.status(400).body("Funcionário já existe");

    }
}

@PutMapping("/Update")
public ResponseEntity<?> updateFuncionario(@RequestHeader("Authorization") String token, @RequestBody FuncionarioDTO.UpdateProfile funcionarioForm){
   
        Funcionario funcionarioformtoken = authService.getFuncionarioFromToken(token);
        
    try {
        if (!authService.authenticate(token)) {
            return ResponseEntity.status(401).body("Token inválido");
        }
    

        Funcionario funcionario = new Funcionario();
        funcionario.setCpf(funcionarioForm.getCpf());
        funcionario.setEmail(funcionarioForm.getEmail().toLowerCase());
        funcionario.setNivelPermissao(funcionarioformtoken.getNivelPermissao());
        funcionario.setNome(funcionarioForm.getNome());
        Optional<Cargo> cargoOpt = cargoService.findById(funcionarioForm.getCargo());
           
        if (cargoOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Cargo não encontrado");
            }
            funcionario.setCargo(cargoOpt.get());

        
        // Criar o funcionário somente na empresa do admin
        
        // Pesquisa se o setor existe
        Optional<Setor> setorFromFuncionarioFormDTO = this.setorService.buscarPorId(funcionarioForm.getSetor());

        if (setorFromFuncionarioFormDTO.isPresent()) {
                Setor setorFromFuncionarioForm = setorFromFuncionarioFormDTO.get();
                funcionario.setSetor(setorFromFuncionarioForm);
       }else{
            return ResponseEntity.status(404).body("Setor não encontrado");
       }

        this.funcionarioService.update(funcionario,funcionarioformtoken.getId().longValue());
        return ResponseEntity.status(201).body("funcionário Salvo");
        
    } catch (Exception e) {
            return ResponseEntity.status(400).body("Operação inválida");

    }
}


@PutMapping("/UpdatePassword")
public ResponseEntity<?> updateFuncionarioSenha( @RequestHeader("Authorization") String token, @RequestBody FuncionarioDTO.UpdatePassword senhaDTO) {

    try {
        Funcionario funcionarioformtoken = authService.getFuncionarioFromToken(token);

        if (!authService.authenticate(token)) {
            return ResponseEntity.status(401).body("Token inválido");
        }

        if (funcionarioformtoken.getSenha().equals(senhaDTO.getOldPassword())) {
            funcionarioService.updatePassword(senhaDTO.getNewPassword().toString(),funcionarioformtoken.getId().longValue());

            return ResponseEntity.status(200).body("Senha atualizada com sucesso");
        } else {
            return ResponseEntity.status(403).body("Senha antiga incorreta");
        }

    } catch (Exception e) {
        return ResponseEntity.status(400).body("Erro ao atualizar senha: " + e.getMessage());
    }
}



@GetMapping("/GetFuncionarioFromEmpresa")
public ResponseEntity<?> getFuncionarioFromEmpresa(@RequestParam Long id, @RequestHeader("Authorization") String token) {

    try {
        if (!authService.authenticate(token)) {
            return ResponseEntity.status(403).body("Token inválido");
        }
         if (authService.getFuncionarioFromToken(token).getNivelPermissao() != 1) {
            return ResponseEntity.status(403).body("Sem permissão");
        }
    
         if (authService.getFuncionarioFromToken(token).getEmpresa().getId().intValue() != id) {
            return ResponseEntity.status(403).body("Sem permissão");
        }


        List<Funcionario> funcionariosList = funcionarioService.findByEmpresaId(id);

        if (!funcionariosList.isEmpty()) {

            //Gera uma lista que passa pelo DTO do funcionário, assim entrgando somente os campos escolhidos 

            List<FuncionarioDTO.Response> funcionariosDTO = funcionariosList.stream()
            

            .map(f -> new FuncionarioDTO.Response(
                null,
                f.getNome(),
                f.getEmail(),
                f.getNivelPermissao(),
                f.getCargo().getNome(),
                f.getSetor().getNome(),
                f.getEmpresa().getNome()
            ))
            .collect(Collectors.toList());

            return ResponseEntity.ok(funcionariosDTO);
        } else {
            return ResponseEntity.status(404).body("Funcionários inexistentes");
        }
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
    }
}

}



