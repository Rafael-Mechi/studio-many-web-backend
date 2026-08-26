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

    @ManyToOne
    @JoinColumn(name = "agendamento_id")
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(name = "servico_id")
    private Servico servico;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCheckinEm() {
        return checkinEm;
    }

    public void setCheckinEm(LocalDateTime checkinEm) {
        this.checkinEm = checkinEm;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }
}