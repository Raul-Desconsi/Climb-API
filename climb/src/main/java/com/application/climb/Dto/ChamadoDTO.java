package com.application.climb.Dto;

public class ChamadoDTO {
    private String motivo;
    private String areaAfetada;
    private String descricao;
    private String urgencia;
    private Integer setorId;
    private Integer responsavelAberturaId; 

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getAreaAfetada() { return areaAfetada; }
    public void setAreaAfetada(String areaAfetada) { this.areaAfetada = areaAfetada; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getUrgencia() { return urgencia; }
    public void setUrgencia(String urgencia) { this.urgencia = urgencia; }

    public Integer getSetorId() { return setorId; }
    public void setSetorId(Integer setorId) { this.setorId = setorId; }

    public Integer getResponsavelAberturaId() { return responsavelAberturaId; }
    public void setResponsavelAberturaId(Integer responsavelAberturaId) { this.responsavelAberturaId = responsavelAberturaId; }
}
