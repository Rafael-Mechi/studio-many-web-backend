package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "servicos")
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "duracao_minutos")
    private Integer duracaoMinutos;

    @Column(name = "preco")
    private Double preco;

    @Column(name = "sinal_valor")
    private Double sinalValor;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @ManyToOne
    @JoinColumn(name = "tipos_sinais_id")
    private TipoSinal tipoSinal;

    public Servico() {}

    public Servico(Long id, String nome, String descricao, String fotoUrl, Integer duracaoMinutos, Double preco, Double sinalValor, Boolean ativo, LocalDateTime criadoEm, TipoSinal tipoSinal) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.fotoUrl = fotoUrl;
        this.duracaoMinutos = duracaoMinutos;
        this.preco = preco;
        this.sinalValor = sinalValor;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
        this.tipoSinal = tipoSinal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public Integer getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(Integer duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }
    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
    public Double getSinalValor() { return sinalValor; }
    public void setSinalValor(Double sinalValor) { this.sinalValor = sinalValor; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public TipoSinal getTipoSinal() { return tipoSinal; }
    public void setTipoSinal(TipoSinal tipoSinal) { this.tipoSinal = tipoSinal; }
}