package many.studio.web_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comprovante_prv")
public class ComprovantePrv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String pdf;

    @ManyToOne
    private Usuario usuario;

    public ComprovantePrv(){}

    public ComprovantePrv(Long id, String pdf, Usuario usuario) {
        this.id = id;
        this.pdf = pdf;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
