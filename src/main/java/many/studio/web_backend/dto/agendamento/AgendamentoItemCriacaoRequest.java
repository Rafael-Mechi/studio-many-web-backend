package many.studio.web_backend.dto.agendamento;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AgendamentoItemCriacaoRequest {

    @NotNull
    private Long servicoId;

    @NotNull
    private Long profissionalId;

    private LocalDateTime inicioAtendimento;
    private LocalDateTime fimAtendimento;
    private Double preco;
    private Double descontoPorcentagem;
    private Double precoFinal;

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public LocalDateTime getInicioAtendimento() {
        return inicioAtendimento;
    }

    public void setInicioAtendimento(LocalDateTime inicioAtendimento) {
        this.inicioAtendimento = inicioAtendimento;
    }

    public LocalDateTime getFimAtendimento() {
        return fimAtendimento;
    }

    public void setFimAtendimento(LocalDateTime fimAtendimento) {
        this.fimAtendimento = fimAtendimento;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getDescontoPorcentagem() {
        return descontoPorcentagem;
    }

    public void setDescontoPorcentagem(Double descontoPorcentagem) {
        this.descontoPorcentagem = descontoPorcentagem;
    }

    public Double getPrecoFinal() {
        return precoFinal;
    }

    public void setPrecoFinal(Double precoFinal) {
        this.precoFinal = precoFinal;
    }
}