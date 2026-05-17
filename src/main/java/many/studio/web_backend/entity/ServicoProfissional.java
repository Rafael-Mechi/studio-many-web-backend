package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "servicos_profissionais")
public class ServicoProfissional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "servicos_id")
    private Servico servico;

    @ManyToOne
    @JoinColumn(name = "profissionais_id")
    private Profissional profissional;

    public ServicoProfissional() {}

    public ServicoProfissional(Long id, Servico servico, Profissional profissional) {
        this.id = id;
        this.servico = servico;
        this.profissional = profissional;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }
    public Profissional getProfissional() { return profissional; }
    public void setProfissional(Profissional profissional) { this.profissional = profissional; }
}