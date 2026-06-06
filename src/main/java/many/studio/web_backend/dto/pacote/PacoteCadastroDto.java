package many.studio.web_backend.dto.pacote;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import many.studio.web_backend.entity.Servico;

import java.time.LocalDateTime;

public class PacoteCadastroDto {

    @NotBlank
    private String nome;

    @Positive
    @Size(min = 1, max = 1000)
    @NotNull
    private Integer totalSessoes;

    @Positive
    @DecimalMax(value = "100000.0")
    @DecimalMin(value = "0.0")
    @NotNull
    private Double precoTotal;


    @Positive
    @Size(min = 1,max = 366)
    @NotNull
    private Integer validadeDias;

    @NotNull
    private Boolean ativo;

    @NotNull
    @PastOrPresent
    private LocalDateTime criadoEm;

    @NotNull
    private Servico servico;

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
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
