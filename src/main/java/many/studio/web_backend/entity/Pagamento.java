package many.studio.web_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
}