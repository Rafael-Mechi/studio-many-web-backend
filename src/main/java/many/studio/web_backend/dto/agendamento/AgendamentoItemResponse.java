package many.studio.web_backend.dto.agendamento;

import many.studio.web_backend.entity.enums.StatusAgendamentoItem;

import java.time.LocalDateTime;

public class AgendamentoItemResponse {

    private Long id;
    private LocalDateTime dataHora;
    private Double precoUnitario;
    private StatusAgendamentoItem status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public StatusAgendamentoItem getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamentoItem status) {
        this.status = status;
    }
}
