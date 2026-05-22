package many.studio.web_backend.dto.agendamento;

import java.time.LocalDateTime;

public class AgendamentoItemResponse {

    private Long id;
    private LocalDateTime inicioAtendimento;
    private LocalDateTime fimAtendimento;
    private LocalDateTime checkinEm;
    private Double preco;
    private Double descontoPorcentagem;
    private Double precoFinal;
    private Long servicoId;
    private String servicoNome;
    private Long profissionalId;
    private String profissionalNome;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getInicioAtendimento() { return inicioAtendimento; }
    public void setInicioAtendimento(LocalDateTime inicioAtendimento) { this.inicioAtendimento = inicioAtendimento; }
    public LocalDateTime getFimAtendimento() { return fimAtendimento; }
    public void setFimAtendimento(LocalDateTime fimAtendimento) { this.fimAtendimento = fimAtendimento; }
    public LocalDateTime getCheckinEm() { return checkinEm; }
    public void setCheckinEm(LocalDateTime checkinEm) { this.checkinEm = checkinEm; }
    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
    public Double getDescontoPorcentagem() { return descontoPorcentagem; }
    public void setDescontoPorcentagem(Double descontoPorcentagem) { this.descontoPorcentagem = descontoPorcentagem; }
    public Double getPrecoFinal() { return precoFinal; }
    public void setPrecoFinal(Double precoFinal) { this.precoFinal = precoFinal; }
    public Long getServicoId() { return servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
    public String getServicoNome() { return servicoNome; }
    public void setServicoNome(String servicoNome) { this.servicoNome = servicoNome; }
    public Long getProfissionalId() { return profissionalId; }
    public void setProfissionalId(Long profissionalId) { this.profissionalId = profissionalId; }
    public String getProfissionalNome() { return profissionalNome; }
    public void setProfissionalNome(String profissionalNome) { this.profissionalNome = profissionalNome; }
}