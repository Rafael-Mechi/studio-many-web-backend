package many.studio.web_backend.dto.agendamento;

public class TotalGastoItem {
    private Long agendamentoId;
    private String estado;
    private Double preco;

    public TotalGastoItem() {}

    public TotalGastoItem(Long agendamentoId, String estado, Double preco) {
        this.agendamentoId = agendamentoId;
        this.estado = estado;
        this.preco = preco;
    }

    public Long getAgendamentoId() { return agendamentoId; }
    public void setAgendamentoId(Long agendamentoId) { this.agendamentoId = agendamentoId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
}
