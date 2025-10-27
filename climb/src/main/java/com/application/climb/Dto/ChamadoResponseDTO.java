package com.application.climb.Dto;

import java.time.LocalDateTime;
import com.application.climb.Model.Chamado;

public class ChamadoResponseDTO {
    private Integer id;
    private String motivo;
    private String descricao;
    private LocalDateTime data;

    private String areaAfetada;
    private String responsavelAbertura;
    private String responsavelResolucao;
    private String urgencia;
    private String setor;
    private String status;

    public ChamadoResponseDTO(Chamado chamado) {
        this.id = chamado.getId();
        this.motivo = chamado.getMotivo();
        this.descricao = chamado.getDescricao();
        this.data = chamado.getData();

        this.areaAfetada = chamado.getAreaAfetada() != null ? chamado.getAreaAfetada().getNome() : null;
        this.responsavelAbertura = chamado.getResponsavelAbertura() != null ? chamado.getResponsavelAbertura().getNome() : null;
        this.responsavelResolucao = chamado.getResponsavelResolucao() != null ? chamado.getResponsavelResolucao().getNome() : null;
        this.urgencia = chamado.getUrgencia() != null ? chamado.getUrgencia().getNome() : null;
        this.setor = chamado.getSetor() != null ? chamado.getSetor().getNome() : null;
        this.status = chamado.getStatus() != null ? chamado.getStatus().getNome() : null;
    }

    // getters e setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public String getAreaAfetada() { return areaAfetada; }
    public void setAreaAfetada(String areaAfetada) { this.areaAfetada = areaAfetada; }

    public String getResponsavelAbertura() { return responsavelAbertura; }
    public void setResponsavelAbertura(String responsavelAbertura) { this.responsavelAbertura = responsavelAbertura; }

    public String getResponsavelResolucao() { return responsavelResolucao; }
    public void setResponsavelResolucao(String responsavelResolucao) { this.responsavelResolucao = responsavelResolucao; }

    public String getUrgencia() { return urgencia; }
    public void setUrgencia(String urgencia) { this.urgencia = urgencia; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
