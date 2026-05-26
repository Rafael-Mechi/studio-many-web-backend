package many.studio.web_backend.dto.agendamento;

import java.time.LocalDateTime;

public class AgendamentoItemResponse {

    private Long id;
    private LocalDateTime inicioAtendimento;
    private LocalDateTime fimAtendimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getInicioAtendimento() {
        return inicioAtendimento;
    }

    public void setInicioAtendimento(LocalDateTime inicioAtendimento) {
        this.inicioAtendimento = inicioAtendimento;
    }

    public LocalDateTime getFimAtendimento() {
        return fimAtendimento;
    }

    public void setFimAtendimento(LocalDateTime fimAtendimento) {
        this.fimAtendimento = fimAtendimento;
    }
}