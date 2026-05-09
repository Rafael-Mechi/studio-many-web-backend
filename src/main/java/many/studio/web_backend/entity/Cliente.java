package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "documento")
    private String documento;

    @Column(name = "total_no_shows")
    private Integer totalNoShows;

    @Column(name = "bloqueado_motivo")
    private String bloqueadoMotivo;

    @Column(name = "lgpd_consentimento")
    private Boolean lgpdConsentimento;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "ativo")
    private Boolean ativo;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Cliente() {}

    public Cliente(Integer id, String nome, String telefone, String documento, Integer totalNoShows, String bloqueadoMotivo, Boolean lgpdConsentimento, LocalDateTime criadoEm, Boolean ativo, Usuario usuario) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.documento = documento;
        this.totalNoShows = totalNoShows;
        this.bloqueadoMotivo = bloqueadoMotivo;
        this.lgpdConsentimento = lgpdConsentimento;
        this.criadoEm = criadoEm;
        this.ativo = ativo;
        this.usuario = usuario;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public Integer getTotalNoShows() { return totalNoShows; }
    public void setTotalNoShows(Integer totalNoShows) { this.totalNoShows = totalNoShows; }
    public String getBloqueadoMotivo() { return bloqueadoMotivo; }
    public void setBloqueadoMotivo(String bloqueadoMotivo) { this.bloqueadoMotivo = bloqueadoMotivo; }
    public Boolean getLgpdConsentimento() { return lgpdConsentimento; }
    public void setLgpdConsentimento(Boolean lgpdConsentimento) { this.lgpdConsentimento = lgpdConsentimento; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}