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

    @Column(name = "cancelamento_motivo")
    private String cancelamentoMotivo;

    @Column(name = "cancelado_em")
    private LocalDateTime canceladoEm;

    private Integer qtdRemarcacoes;

    @Column(name = "criado_em")
    @CreationTimestamp
    private LocalDateTime criadoEm;

    @Column(name = "preco")
    private Double preco;

    @Column(name = "desconto_porcentagem")
    private Double descontoPorcentagem;

    @Column(name = "preco_final")
    private Double precoFinal;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "pacote_id")
    private Pacote pacote;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    @ManyToOne
    @JoinColumn(name = "status_agendamento_id")
    private StatusAgendamento statusAgendamento;

    @ManyToOne
    @JoinColumn(name = "criado_por_usuario_id")
    private Usuario criadoPorUsuario;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCancelamentoMotivo() {
        return cancelamentoMotivo;
    }

    public void setCancelamentoMotivo(String cancelamentoMotivo) {
        this.cancelamentoMotivo = cancelamentoMotivo;
    }

    public LocalDateTime getCanceladoEm() {
        return canceladoEm;
    }

    public void setCanceladoEm(LocalDateTime canceladoEm) {
        this.canceladoEm = canceladoEm;
    }

    public Integer getQtdRemarcacoes() {
        return qtdRemarcacoes;
    }

    public void setQtdRemarcacoes(Integer qtdRemarcacoes) {
        this.qtdRemarcacoes = qtdRemarcacoes;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Pacote getPacote() {
        return pacote;
    }

    public void setPacote(Pacote pacote) {
        this.pacote = pacote;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public StatusAgendamento getStatusAgendamento() {
        return statusAgendamento;
    }

    public void setStatusAgendamento(StatusAgendamento statusAgendamento) {
        this.statusAgendamento = statusAgendamento;
    }

    public Usuario getCriadoPorUsuario() {
        return criadoPorUsuario;
    }

    public void setCriadoPorUsuario(Usuario criadoPorUsuario) {
        this.criadoPorUsuario = criadoPorUsuario;
    }
}