package com.application.climb.Service;

import java.util.Date;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.climb.Model.Funcionario;
import com.application.climb.Repository.FuncionarioRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Service
public class AuthService {

    @Autowired
    FuncionarioRepository funcionarioRepository;

    // Chave para criptografia 
    private final String secretKeyString = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }


    // Criação de token para login  e validação de email 
    public Optional<String> login(Funcionario funcionarioFromWeb) {
        Optional<Funcionario> funcionarioOPT = this.funcionarioRepository.findByEmail(funcionarioFromWeb.getEmail().trim().toLowerCase());

        if (funcionarioOPT.isEmpty()) {
            throw new RuntimeException("Email ou senha incorretos");
        }

        Funcionario funcionario = funcionarioOPT.get();

        if (!funcionario.getSenha().equals(funcionarioFromWeb.getSenha())) {
            throw new RuntimeException("Email ou senha incorretos");
        }


        String token = Jwts.builder()
                .setSubject(funcionario.getId().toString())         
                .claim("email", funcionario.getEmail())            
                .claim("nivelPermissao", funcionario.getNivelPermissao())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();

        return Optional.of(token);
    }

    // Autenticação de token baseado na secretKeyString
    public boolean authenticate(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
 //Pega o usuario baseado no seu token
public Funcionario getFuncionarioFromToken(String token) {
    try {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey()) 
                .build()
                .parseClaimsJws(token)
                .getBody();

        Optional<Funcionario> funcionarioOPT = this.funcionarioRepository.findById(Long.parseLong(claims.getSubject()));

        if (funcionarioOPT.isPresent()) {
            Funcionario funcionario = funcionarioOPT.get();
            return funcionario;
        } else {
            throw new RuntimeException("Funcionário não encontrado para o token fornecido");
        }

    } catch (Exception e) {
        throw new RuntimeException("Token inválido ou expirado", e);
    }
}
}


