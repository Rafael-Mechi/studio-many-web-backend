package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "profissionais")
public class Profissional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "foto_url")
    private String fotoUrl;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Profissional() {}

    public Profissional(Integer id, String nome, String fotoUrl, LocalDateTime criadoEm, Usuario usuario) {
        this.id = id;
        this.nome = nome;
        this.fotoUrl = fotoUrl;
        this.usuario = usuario;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}