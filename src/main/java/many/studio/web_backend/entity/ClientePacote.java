package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente_pacotes")
public class ClientePacote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sessoes_restantes")
    private Integer sessoesRestantes;

    @Column(name = "valido_ate")
    private LocalDateTime validoAte;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "pacote_id")
    private Pacote pacote;

    @ManyToOne
    @JoinColumn(name = "pagamento_id")
    private Pagamento pagamento;

    @ManyToOne
    @JoinColumn(name = "status_cliente_pacote_id")
    private StatusClientePacote statusClientePacote;

    public ClientePacote() {}

    public ClientePacote(Integer id, Integer sessoesRestantes, LocalDateTime validoAte, LocalDateTime criadoEm, Cliente cliente, Pacote pacote, Pagamento pagamento, StatusClientePacote statusClientePacote) {
        this.id = id;
        this.sessoesRestantes = sessoesRestantes;
        this.validoAte = validoAte;
        this.criadoEm = criadoEm;
        this.cliente = cliente;
        this.pacote = pacote;
        this.pagamento = pagamento;
        this.statusClientePacote = statusClientePacote;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getSessoesRestantes() { return sessoesRestantes; }
    public void setSessoesRestantes(Integer sessoesRestantes) { this.sessoesRestantes = sessoesRestantes; }
    public LocalDateTime getValidoAte() { return validoAte; }
    public void setValidoAte(LocalDateTime validoAte) { this.validoAte = validoAte; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Pacote getPacote() { return pacote; }
    public void setPacote(Pacote pacote) { this.pacote = pacote; }
    public Pagamento getPagamento() { return pagamento; }
    public void setPagamento(Pagamento pagamento) { this.pagamento = pagamento; }
    public StatusClientePacote getStatusClientePacote() { return statusClientePacote; }
    public void setStatusClientePacote(StatusClientePacote statusClientePacote) { this.statusClientePacote = statusClientePacote; }
}