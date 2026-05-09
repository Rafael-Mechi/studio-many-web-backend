package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento_itens")
public class AgendamentoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "inicio")
    private LocalDateTime inicio;

    @Column(name = "fim")
    private LocalDateTime fim;

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

    public AgendamentoItem() {}

    public AgendamentoItem(Integer id, LocalDateTime inicio, LocalDateTime fim, Double preco, Double descontoPorcentagem, Double precoFinal, Agendamento agendamento, Servico servico, Profissional profissional) {
        this.id = id;
        this.inicio = inicio;
        this.fim = fim;
        this.preco = preco;
        this.descontoPorcentagem = descontoPorcentagem;
        this.precoFinal = precoFinal;
        this.agendamento = agendamento;
        this.servico = servico;
        this.profissional = profissional;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFim() { return fim; }
    public void setFim(LocalDateTime fim) { this.fim = fim; }
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