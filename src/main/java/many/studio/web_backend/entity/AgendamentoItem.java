package many.studio.web_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento_itens")
public class AgendamentoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inicio_atendimento")
    private LocalDateTime inicioAtendimento;

    @Column(name = "fim_atendimento")
    private LocalDateTime fimAtendimento;

    @Column(name = "checkin_em")
    private LocalDateTime checkinEm;

    @Column(name = "preco")
    private Double preco;

    @Column(name = "desconto_porcentagem")
    private Double descontoPorcentagem;

    @Column(name = "preco_final")
    private Double precoFinal;

    @ManyToOne
    @JoinColumn(name = "agendamento_id")
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(name = "servico_id")
    private Servico servico;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

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
    public Agendamento getAgendamento() { return agendamento; }
    public void setAgendamento(Agendamento agendamento) { this.agendamento = agendamento; }
    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }
    public Profissional getProfissional() { return profissional; }
    public void setProfissional(Profissional profissional) { this.profissional = profissional; }
}