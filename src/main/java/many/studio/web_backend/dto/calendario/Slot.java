package many.studio.web_backend.dto.calendario;

import java.time.LocalDateTime;

public class Slot {

    private LocalDateTime inicio;
    private LocalDateTime fim;
    private StatusSlot status;

    public Slot(LocalDateTime inicio, LocalDateTime fim, StatusSlot status) {
        this.inicio = inicio;
        this.fim = fim;
        this.status = status;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public StatusSlot getStatus() {
        return status;
    }

    public void setStatus(StatusSlot status) {
        this.status = status;
    }
}
