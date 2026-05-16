package many.studio.web_backend.entity;

import jakarta.persistence.*;
import many.studio.web_backend.entity.enums.StatusAgendamentoItem;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento_itens")
public class AgendamentoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "preco_unitario")
    private Double precoUnitario;

    @Enumerated(EnumType.STRING)
    private StatusAgendamentoItem status;

    @ManyToOne
    @JoinColumn(name = "agendamento_id")
    private Agendamento agendamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public StatusAgendamentoItem getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamentoItem status) {
        this.status = status;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }
}