package many.studio.web_backend.entity;

import jakarta.persistence.*;

@Entity
public class Comprovante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String arquivo;

    @Column(name = "tipo_arquivo", nullable = false)
    private String tipoArquivo;

    @OneToOne
    @JoinColumn(name = "pagamento_id", nullable = false, unique = true)
    private Pagamento pagamento;

    public Comprovante() {
    }

    public Comprovante(Long id, String arquivo, String tipoArquivo, Pagamento pagamento) {
        this.id = id;
        this.arquivo = arquivo;
        this.tipoArquivo = tipoArquivo;
        this.pagamento = pagamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
