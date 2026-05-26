package many.studio.web_backend.dto.calendario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class DiasDeTrabalhoResponse {

    private LocalDate data;
    private LocalTime inicio;
    private LocalTime fim;

    public DiasDeTrabalhoResponse(LocalDate data, LocalTime inicio, LocalTime fim) {
        this.data = data;
        this.inicio = inicio;
        this.fim = fim;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalTime inicio) {
        this.inicio = inicio;
    }

    public LocalTime getFim() {
        return fim;
    }

    public void setFim(LocalTime fim) {
        this.fim = fim;
    }
}
