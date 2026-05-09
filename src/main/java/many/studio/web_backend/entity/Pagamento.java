package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "pago_em")
    private LocalDateTime pagoEm;

    @Column(name = "comprovante_url")
    private String comprovanteUrl;

    @ManyToOne
    @JoinColumn(name = "agendamento_id")
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(name = "status_pagamento_id")
    private StatusPagamento statusPagamento;

    @ManyToOne
    @JoinColumn(name = "tipo_pagamentos_id")
    private TipoPagamento tipoPagamento;

    public Pagamento() {}

    public Pagamento(Integer id, Double valor, LocalDateTime pagoEm, String comprovanteUrl, Agendamento agendamento, StatusPagamento statusPagamento, TipoPagamento tipoPagamento) {
        this.id = id;
        this.valor = valor;
        this.pagoEm = pagoEm;
        this.comprovanteUrl = comprovanteUrl;
        this.agendamento = agendamento;
        this.statusPagamento = statusPagamento;
        this.tipoPagamento = tipoPagamento;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public LocalDateTime getPagoEm() { return pagoEm; }
    public void setPagoEm(LocalDateTime pagoEm) { this.pagoEm = pagoEm; }
    public String getComprovanteUrl() { return comprovanteUrl; }
    public void setComprovanteUrl(String comprovanteUrl) { this.comprovanteUrl = comprovanteUrl; }
    public Agendamento getAgendamento() { return agendamento; }
    public void setAgendamento(Agendamento agendamento) { this.agendamento = agendamento; }
    public StatusPagamento getStatusPagamento() { return statusPagamento; }
    public void setStatusPagamento(StatusPagamento statusPagamento) { this.statusPagamento = statusPagamento; }
    public TipoPagamento getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }
}