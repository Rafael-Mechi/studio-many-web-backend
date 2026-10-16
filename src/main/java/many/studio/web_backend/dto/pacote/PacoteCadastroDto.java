package many.studio.web_backend.dto.pacote;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public class PacoteCadastroDto {

    @NotBlank
    @Size(min = 1, max = 100)
    @Schema(description = "Nome do pacote", example = "Pacote inverno limpeza de pele")
    private String nome;

    @NotNull
    @Min(2)
    @Max(1000)
    @Schema(description = "Quantidade de sessões do serviço (mínimo 2; a sessão avulsa é criada automaticamente)", example = "8")
    private Integer totalSessoes;

    @NotNull
    @Positive
    @DecimalMax(value = "100000.0")
    @Schema(description = "Preço total do pacote (já com desconto aplicado)", example = "630.00")
    private Double precoTotal;

    @NotNull
    @Min(1)
    @Max(366)
    @Schema(description = "Dias de validade do pacote após a compra", example = "90")
    private Integer validadeDias;

    @NotNull
    @Schema(description = "Se o pacote está disponível para os clientes", example = "true")
    private Boolean ativo;

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
}
