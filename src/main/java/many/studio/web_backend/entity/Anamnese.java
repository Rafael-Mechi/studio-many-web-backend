package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "anamneses")
public class Anamnese {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "informacao")
    private String informacao;

    public Anamnese() {}

    public Anamnese(Long id, String informacao) {
        this.id = id;
        this.informacao = informacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInformacao() { return informacao; }
    public void setInformacao(String informacao) { this.informacao = informacao; }
}
