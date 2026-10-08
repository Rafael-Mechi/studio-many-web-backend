package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "profissionais")
public class Profissional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "documento")
    private String documento;

    @Column(name = "almoco_inicio")
    private LocalTime almocoInicio;

    @Column(name = "almoco_fim")
    private LocalTime almocoFim;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Profissional() {}

    public Profissional(Long id, String nome, String telefone, String documento, Usuario usuario) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.documento = documento;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getDocumento() {
        return documento;
    }
    public void setDocumento(String documento) {
        this.documento = documento;
    }
    public LocalTime getAlmocoInicio() {
        return almocoInicio;
    }
    public void setAlmocoInicio(LocalTime almocoInicio) {
        this.almocoInicio = almocoInicio;
    }
    public LocalTime getAlmocoFim() {
        return almocoFim;
    }
    public void setAlmocoFim(LocalTime almocoFim) {
        this.almocoFim = almocoFim;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}