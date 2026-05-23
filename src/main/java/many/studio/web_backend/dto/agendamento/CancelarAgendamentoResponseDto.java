package many.studio.web_backend.dto.agendamento;

public class CancelarAgendamentoResponseDto {
    private String resposta;

    public CancelarAgendamentoResponseDto(String resposta) {
        this.resposta = resposta;
    }

    public CancelarAgendamentoResponseDto(){}

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}
