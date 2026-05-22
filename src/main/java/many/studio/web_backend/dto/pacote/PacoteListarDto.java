package many.studio.web_backend.dto.pacote;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import many.studio.web_backend.entity.Servico;

public class PacoteListarDto {

    @Schema(description = "Id do pacote", example = "132")
    private Long id;

    @Schema(description = "Nome do pacote", example = "pacote inverno limpeza de pele")
    private String nome;

    @Schema(description = "Número de sessões", example = "8")
    private Integer totalSessoes;

    @Schema(description = "Preço do pacote", example = "630,00")
    private Double precoTotal;

    @Schema(description = "Tempo de validade que o pacote tem antes de inspirar em dias", example = "90")
    private Integer validadeDias;

    @Schema(description = "Se o pacote está disponível", example = "true")
    private Boolean ativo;


    @Schema(description = "Qual é o serviço que esse pacote tem", example = "Limpeza de pele")
    private Servico servico;


    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(Double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Integer getTotalSessoes() {
        return totalSessoes;
    }

    public void setTotalSessoes(Integer totalSessoes) {
        this.totalSessoes = totalSessoes;
    }

    public Integer getValidadeDias() {
        return validadeDias;
    }

    public void setValidadeDias(Integer validadeDias) {
        this.validadeDias = validadeDias;
    }
}
