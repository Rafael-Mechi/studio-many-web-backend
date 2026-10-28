package many.studio.web_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "documento")
    private String documento;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "bloqueado_motivo")
    private String bloqueadoMotivo;

    @Column(name = "lgpd_consentimento")
    private Boolean lgpdConsentimento;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Cliente() {}

    public Cliente(
            Long id,
            String nome,
            String telefone,
            String documento,
            String bloqueadoMotivo,
            Boolean lgpdConsentimento,
            Usuario usuario
    ) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.documento = documento;
        this.bloqueadoMotivo = bloqueadoMotivo;
        this.lgpdConsentimento = lgpdConsentimento;
        this.usuario = usuario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public String getBloqueadoMotivo() { return bloqueadoMotivo; }
    public void setBloqueadoMotivo(String bloqueadoMotivo) { this.bloqueadoMotivo = bloqueadoMotivo; }
    public Boolean getLgpdConsentimento() { return lgpdConsentimento; }
    public void setLgpdConsentimento(Boolean lgpdConsentimento) { this.lgpdConsentimento = lgpdConsentimento; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}