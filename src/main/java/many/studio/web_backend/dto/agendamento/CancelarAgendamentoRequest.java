package many.studio.web_backend.dto.agendamento;

public class CancelarAgendamentoRequest {
    private String motivo;

    public CancelarAgendamentoRequest(String motivo) {
        this.motivo = motivo;
    }

    public CancelarAgendamentoRequest(){}

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
