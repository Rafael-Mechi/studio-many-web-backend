package many.studio.web_backend.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;

@Entity
@Table(name = "tipo_pagamentos")
public class TipoPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo")
    private String tipo;

    public TipoPagamento() {}

    public TipoPagamento(Long id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}