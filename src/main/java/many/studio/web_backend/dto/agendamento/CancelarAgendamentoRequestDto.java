package many.studio.web_backend.dto.agendamento;

public class CancelarAgendamentoRequestDto {
    private String motivo;

    public CancelarAgendamentoRequestDto(String motivo) {
        this.motivo = motivo;
    }

    public CancelarAgendamentoRequestDto(){}

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
