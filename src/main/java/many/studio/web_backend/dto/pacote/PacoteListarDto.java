package many.studio.web_backend.dto.pacote;

import io.swagger.v3.oas.annotations.media.Schema;

public class PacoteListarDto {

    @Schema(description = "Id do pacote", example = "132")
    private Long id;

    @Schema(description = "Nome do pacote", example = "pacote inverno limpeza de pele")
    private String nome;

    @Schema(description = "Número de sessões", example = "8")
    private Integer totalSessoes;

    @Schema(description = "Preço do pacote (com desconto)", example = "630.00")
    private Double precoTotal;

    @Schema(description = "Preço sem desconto: sessões x preço avulso do serviço", example = "700.00")
    private Double precoOriginal;

    @Schema(description = "Desconto do pacote em relação às sessões avulsas, em %", example = "10.0")
    private Double descontoPercentual;

    @Schema(description = "Tempo de validade que o pacote tem antes de expirar, em dias", example = "90")
    private Integer validadeDias;

    @Schema(description = "Se o pacote está disponível", example = "true")
    private Boolean ativo;

    @Schema(description = "Serviço ao qual o pacote pertence")
    private ServicoResumoDto servico;

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

    public Integer getTotalSessoes() {
        return totalSessoes;
    }

    public void setTotalSessoes(Integer totalSessoes) {
        this.totalSessoes = totalSessoes;
    }

    public Double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(Double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public Double getPrecoOriginal() {
        return precoOriginal;
    }

    public void setPrecoOriginal(Double precoOriginal) {
        this.precoOriginal = precoOriginal;
    }

    public Double getDescontoPercentual() {
        return descontoPercentual;
    }

    public void setDescontoPercentual(Double descontoPercentual) {
        this.descontoPercentual = descontoPercentual;
    }

    public Integer getValidadeDias() {
        return validadeDias;
    }

    public void setValidadeDias(Integer validadeDias) {
        this.validadeDias = validadeDias;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public ServicoResumoDto getServico() {
        return servico;
    }

    public void setServico(ServicoResumoDto servico) {
        this.servico = servico;
    }
}
