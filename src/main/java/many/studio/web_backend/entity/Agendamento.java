package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "inicio")
    private LocalDateTime inicio;

    @Column(name = "fim")
    private LocalDateTime fim;

    @Column(name = "cancelamento_motivo")
    private String cancelamentoMotivo;

    @Column(name = "cancelado_em")
    private LocalDateTime canceladoEm;

    @Column(name = "qtd_remarcacoes")
    private Integer qtdRemarcacoes;

    @Column(name = "remarcacao_aprovacao_necessaria")
    private Boolean remarcacaoAprovacaoNecessaria;

    @Column(name = "checkin_em")
    private LocalDateTime checkinEm;

    @Column(name = "inicio_atendimento")
    private LocalDateTime inicioAtendimento;

    @Column(name = "fim_atendimento")
    private LocalDateTime fimAtendimento;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "status_agendamento_id")
    private StatusAgendamento statusAgendamento;

    @ManyToOne
    @JoinColumn(name = "criado_por_usuario_id")
    private Usuario criadoPorUsuario;

    public Agendamento() {}

    public Agendamento(Integer id, LocalDateTime inicio, LocalDateTime fim, String cancelamentoMotivo, LocalDateTime canceladoEm, Integer qtdRemarcacoes, Boolean remarcacaoAprovacaoNecessaria, LocalDateTime checkinEm, LocalDateTime inicioAtendimento, LocalDateTime fimAtendimento, LocalDateTime criadoEm, Cliente cliente, StatusAgendamento statusAgendamento, Usuario criadoPorUsuario) {
        this.id = id;
        this.inicio = inicio;
        this.fim = fim;
        this.cancelamentoMotivo = cancelamentoMotivo;
        this.canceladoEm = canceladoEm;
        this.qtdRemarcacoes = qtdRemarcacoes;
        this.remarcacaoAprovacaoNecessaria = remarcacaoAprovacaoNecessaria;
        this.checkinEm = checkinEm;
        this.inicioAtendimento = inicioAtendimento;
        this.fimAtendimento = fimAtendimento;
        this.criadoEm = criadoEm;
        this.cliente = cliente;
        this.statusAgendamento = statusAgendamento;
        this.criadoPorUsuario = criadoPorUsuario;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFim() { return fim; }
    public void setFim(LocalDateTime fim) { this.fim = fim; }
    public String getCancelamentoMotivo() { return cancelamentoMotivo; }
    public void setCancelamentoMotivo(String cancelamentoMotivo) { this.cancelamentoMotivo = cancelamentoMotivo; }
    public LocalDateTime getCanceladoEm() { return canceladoEm; }
    public void setCanceladoEm(LocalDateTime canceladoEm) { this.canceladoEm = canceladoEm; }
    public Integer getQtdRemarcacoes() { return qtdRemarcacoes; }
    public void setQtdRemarcacoes(Integer qtdRemarcacoes) { this.qtdRemarcacoes = qtdRemarcacoes; }
    public Boolean getRemarcacaoAprovacaoNecessaria() { return remarcacaoAprovacaoNecessaria; }
    public void setRemarcacaoAprovacaoNecessaria(Boolean remarcacaoAprovacaoNecessaria) { this.remarcacaoAprovacaoNecessaria = remarcacaoAprovacaoNecessaria; }
    public LocalDateTime getCheckinEm() { return checkinEm; }
    public void setCheckinEm(LocalDateTime checkinEm) { this.checkinEm = checkinEm; }
    public LocalDateTime getInicioAtendimento() { return inicioAtendimento; }
    public void setInicioAtendimento(LocalDateTime inicioAtendimento) { this.inicioAtendimento = inicioAtendimento; }
    public LocalDateTime getFimAtendimento() { return fimAtendimento; }
    public void setFimAtendimento(LocalDateTime fimAtendimento) { this.fimAtendimento = fimAtendimento; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public StatusAgendamento getStatusAgendamento() { return statusAgendamento; }
    public void setStatusAgendamento(StatusAgendamento statusAgendamento) { this.statusAgendamento = statusAgendamento; }
    public Usuario getCriadoPorUsuario() { return criadoPorUsuario; }
    public void setCriadoPorUsuario(Usuario criadoPorUsuario) { this.criadoPorUsuario = criadoPorUsuario; }
}