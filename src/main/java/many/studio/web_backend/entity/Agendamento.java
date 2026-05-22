package many.studio.web_backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "criado_em")
    @CreationTimestamp
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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public StatusAgendamento getStatusAgendamento() { return statusAgendamento; }
    public void setStatusAgendamento(StatusAgendamento statusAgendamento) { this.statusAgendamento = statusAgendamento; }
    public Usuario getCriadoPorUsuario() { return criadoPorUsuario; }
    public void setCriadoPorUsuario(Usuario criadoPorUsuario) { this.criadoPorUsuario = criadoPorUsuario; }
}