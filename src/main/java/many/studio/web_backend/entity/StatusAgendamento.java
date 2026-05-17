package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "status_agendamentos")
public class StatusAgendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estado")
    private String estado;

    public StatusAgendamento() {}

    public StatusAgendamento(Long id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}