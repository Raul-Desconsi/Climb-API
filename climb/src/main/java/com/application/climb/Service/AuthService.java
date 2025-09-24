package com.application.climb.Service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.climb.Model.Funcionario;
import com.application.climb.Repository.FuncionarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;




@Service
public class AuthService {


    @Autowired
    FuncionarioRepository funcionarioRepository;

    //  JWT simples 
    private String secretKey = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";

    
    public Optional<String> login(Funcionario funcionarioFromWeb) {
        
        
        Optional<Funcionario> funcionarioOPT = this.funcionarioRepository.findByEmail(funcionarioFromWeb.getEmail().trim().toLowerCase());
        Funcionario funcionario = funcionarioOPT.get();

            if (funcionarioOPT.isPresent() && funcionario.getSenha().equals(funcionarioFromWeb.getSenha())) {

                if (funcionario.getNivelPermissao() >= 3 ) {
                    throw new RuntimeException("Sem permissão");
                }

                    String token = Jwts.builder()
                            .setSubject(funcionario.getEmail())
                            .setIssuedAt(new Date())
                            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h 
                            .signWith(SignatureAlgorithm.HS256, secretKey)
                            .compact();
                    return Optional.of(token);
                }
                else{
                   throw new RuntimeException("Email ou senha incorretos");
                } 
            }
            
        
    }
