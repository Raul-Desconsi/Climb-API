package com.application.climb.Dto;

import java.time.LocalDateTime;

import com.application.climb.Model.Chamado;

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

    // 🔹 Campos auxiliares para exibição e filtro
    private String statusNome;
    private String urgenciaNome;
    private String responsavelNome;

    public ChamadoDTO() {}

    // 🔹 Construtor para converter de entidade para DTO
    public ChamadoDTO(Chamado chamado) {
        this.id = chamado.getId();
        this.motivo = chamado.getMotivo();
        this.descricao = chamado.getDescricao();
        this.data = chamado.getData();

        if (chamado.getAreaAfetada() != null)
            this.areaAfetadaId = chamado.getAreaAfetada().getId();

        if (chamado.getResponsavelAbertura() != null) {
            this.responsavelAberturaId = chamado.getResponsavelAbertura().getId().intValue();
            this.responsavelNome = chamado.getResponsavelAbertura().getNome();
        }

        if (chamado.getResponsavelResolucao() != null)
            this.responsavelResolucaoId = chamado.getResponsavelResolucao().getId().intValue();

        if (chamado.getUrgencia() != null) {
            this.urgenciaId = chamado.getUrgencia().getId();
            this.urgenciaNome = chamado.getUrgencia().getNome();
        }

        if (chamado.getSetor() != null)
            this.setorId = chamado.getSetor().getId();

        if (chamado.getStatus() != null) {
            this.statusId = chamado.getStatus().getId();
            this.statusNome = chamado.getStatus().getNome();
        }
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public Integer getAreaAfetadaId() { return areaAfetadaId; }
    public void setAreaAfetadaId(Integer areaAfetadaId) { this.areaAfetadaId = areaAfetadaId; }

    public Integer getResponsavelAberturaId() { return responsavelAberturaId; }
    public void setResponsavelAberturaId(Integer responsavelAberturaId) { this.responsavelAberturaId = responsavelAberturaId; }

    public Integer getResponsavelResolucaoId() { return responsavelResolucaoId; }
    public void setResponsavelResolucaoId(Integer responsavelResolucaoId) { this.responsavelResolucaoId = responsavelResolucaoId; }

    public Integer getUrgenciaId() { return urgenciaId; }
    public void setUrgenciaId(Integer urgenciaId) { this.urgenciaId = urgenciaId; }

    public Integer getSetorId() { return setorId; }
    public void setSetorId(Integer setorId) { this.setorId = setorId; }

    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }

    public String getStatusNome() { return statusNome; }
    public void setStatusNome(String statusNome) { this.statusNome = statusNome; }

    public String getUrgenciaNome() { return urgenciaNome; }
    public void setUrgenciaNome(String urgenciaNome) { this.urgenciaNome = urgenciaNome; }

    public String getResponsavelNome() { return responsavelNome; }
    public void setResponsavelNome(String responsavelNome) { this.responsavelNome = responsavelNome; }
}
