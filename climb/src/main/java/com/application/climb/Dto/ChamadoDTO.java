package com.application.climb.Dto;

import java.time.LocalDateTime;

public class ChamadoDTO {

    private Integer id;
    private String motivo;
    private String descricao;
    private LocalDateTime data;

    private Integer areaAfetadaId;
    private Integer responsavelAberturaId;
    private Integer responsavelResolucaoId;
    private Integer urgenciaId;
    private Integer setorId;
    private Integer statusId;


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getData() {
        return data;
    }
    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Integer getAreaAfetadaId() {
        return areaAfetadaId;
    }
    public void setAreaAfetadaId(Integer areaAfetadaId) {
        this.areaAfetadaId = areaAfetadaId;
    }

    public Integer getResponsavelAberturaId() {
        return responsavelAberturaId;
    }
    public void setResponsavelAberturaId(Integer responsavelAberturaId) {
        this.responsavelAberturaId = responsavelAberturaId;
    }

    public Integer getResponsavelResolucaoId() {
        return responsavelResolucaoId;
    }
    public void setResponsavelResolucaoId(Integer responsavelResolucaoId) {
        this.responsavelResolucaoId = responsavelResolucaoId;
    }

    public Integer getUrgenciaId() {
        return urgenciaId;
    }
    public void setUrgenciaId(Integer urgenciaId) {
        this.urgenciaId = urgenciaId;
    }

    public Integer getSetorId() {
        return setorId;
    }
    public void setSetorId(Integer setorId) {
        this.setorId = setorId;
    }

    public Integer getStatusId() {
        return statusId;
    }
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}
