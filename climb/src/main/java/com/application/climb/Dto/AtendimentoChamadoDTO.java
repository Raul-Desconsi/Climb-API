package com.application.climb.Dto;

import java.time.LocalDate;

public class AtendimentoChamadoDTO {

    private Integer id;
    private String resposta;
    private Integer conclusaoChamado; // 0 ou 1 conforme seu model
    private LocalDate dataAtendimento;

    private Integer setorDirecionadoId;
    private Integer setorAtendimentoId;
    private Integer responsavelAtendimentoId;
    private Integer chamadoId;

    // getters e setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getResposta() { return resposta; }
    public void setResposta(String resposta) { this.resposta = resposta; }

    public Integer getConclusaoChamado() { return conclusaoChamado; }
    public void setConclusaoChamado(Integer conclusaoChamado) { this.conclusaoChamado = conclusaoChamado; }

    public LocalDate getDataAtendimento() { return dataAtendimento; }
    public void setDataAtendimento(LocalDate dataAtendimento) { this.dataAtendimento = dataAtendimento; }

    public Integer getSetorDirecionadoId() { return setorDirecionadoId; }
    public void setSetorDirecionadoId(Integer setorDirecionadoId) { this.setorDirecionadoId = setorDirecionadoId; }

    public Integer getSetorAtendimentoId() { return setorAtendimentoId; }
    public void setSetorAtendimentoId(Integer setorAtendimentoId) { this.setorAtendimentoId = setorAtendimentoId; }

    public Integer getResponsavelAtendimentoId() { return responsavelAtendimentoId; }
    public void setResponsavelAtendimentoId(Integer responsavelAtendimentoId) { this.responsavelAtendimentoId = responsavelAtendimentoId; }

    public Integer getChamadoId() { return chamadoId; }
    public void setChamadoId(Integer chamadoId) { this.chamadoId = chamadoId; }
}
