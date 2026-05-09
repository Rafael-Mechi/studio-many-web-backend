package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "anamneses")
public class Anamnese {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "informacao")
    private String informacao;

    public Anamnese() {}

    public Anamnese(Integer id, String informacao) {
        this.id = id;
        this.informacao = informacao;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getInformacao() { return informacao; }
    public void setInformacao(String informacao) { this.informacao = informacao; }
}
