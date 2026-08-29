package many.studio.web_backend.dto.pagamento;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PagamentoRequest {

    @NotNull
    @Positive
    private Double valor;

    @NotNull
    private Long agendamentoId;

    @NotNull
    private Long statusPagamentoId;

    @NotNull
    private Long tipoPagamentoId;

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Long getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(Long agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public Long getStatusPagamentoId() {
        return statusPagamentoId;
    }

    public void setStatusPagamentoId(Long statusPagamentoId) {
        this.statusPagamentoId = statusPagamentoId;
    }

    public Long getTipoPagamentoId() {
        return tipoPagamentoId;
    }

    public void setTipoPagamentoId(Long tipoPagamentoId) {
        this.tipoPagamentoId = tipoPagamentoId;
    }
}
