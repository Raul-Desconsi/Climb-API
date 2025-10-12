package com.application.climb.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class FuncionarioDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Create {
        private String cpf;
        private String nome;
        private String email;
        private String senha;
        private int nivelPermissao;
        private String cargo;
        private int setor;
        private int empresa;
    }

    
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        private String nome;
        private String email;
        private int nivelPermissao;
        private String cargo;
        private String setor;
        private String empresa;

       
    }
}
