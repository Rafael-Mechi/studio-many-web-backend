package many.studio.web_backend.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;

@Entity
@Table(name = "tipo_pagamentos")
public class TipoPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo")
    private String tipo;

    public TipoPagamento() {}

    public TipoPagamento(Integer id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}