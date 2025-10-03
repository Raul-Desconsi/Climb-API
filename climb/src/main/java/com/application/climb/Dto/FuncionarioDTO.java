package com.application.climb.Dto;

public class FuncionarioDTO {

    private String cpf;
    private String nome;
    private String email;
    private String senha;
    private int nivelPermissao;
    private String funcao;
    private int setor;
    private int empresa;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public int getNivelPermissao() { return nivelPermissao; }
    public void setNivelPermissao(int nivelPermissao) { this.nivelPermissao = nivelPermissao; }

    public String getFuncao() { return funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }

    public Integer getSetor() { return setor; }
    public void setSetor(int setor) { this.setor = setor; }

    public int getEmpresa() { return empresa; }
    public void setEmpresa(int empresa) { this.empresa = empresa; }


    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

}
