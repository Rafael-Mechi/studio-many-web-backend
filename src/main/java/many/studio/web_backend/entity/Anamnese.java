package many.studio.web_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "anamneses")
public class Anamnese {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "informacao")
    private String informacao;

    @Column(name = "arquivo_url")
    private String arquivoUrl;

    public Anamnese() {}

    public Anamnese(Long id, String informacao, String arquivoUrl) {
        this.id = id;
        this.informacao = informacao;
        this.arquivoUrl = arquivoUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public String getArquivoUrl() {
        return arquivoUrl;
    }

    public void setArquivoUrl(String arquivoUrl) {
        this.arquivoUrl = arquivoUrl;
    }
}
