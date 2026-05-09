package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pacotes")
public class Pacote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "total_sessoes")
    private Integer totalSessoes;

    @Column(name = "preco_total")
    private Double precoTotal;

    @Column(name = "validade_dias")
    private Integer validadeDias;

    @Column(name = "ativo")
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "servicos_id")
    private Servico servico;

    public Pacote() {}

    public Pacote(Integer id, String nome, Integer totalSessoes, Double precoTotal, Integer validadeDias, Boolean ativo, Servico servico) {
        this.id = id;
        this.nome = nome;
        this.totalSessoes = totalSessoes;
        this.precoTotal = precoTotal;
        this.validadeDias = validadeDias;
        this.ativo = ativo;
        this.servico = servico;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Integer getTotalSessoes() { return totalSessoes; }
    public void setTotalSessoes(Integer totalSessoes) { this.totalSessoes = totalSessoes; }
    public Double getPrecoTotal() { return precoTotal; }
    public void setPrecoTotal(Double precoTotal) { this.precoTotal = precoTotal; }
    public Integer getValidadeDias() { return validadeDias; }
    public void setValidadeDias(Integer validadeDias) { this.validadeDias = validadeDias; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }
}