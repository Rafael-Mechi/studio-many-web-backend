package many.studio.web_backend.dto.config;

import java.time.LocalDateTime;

public class BloqueioResponse {

    private Long id;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private String motivo;

    public BloqueioResponse() {
    }

    public BloqueioResponse(Long id, LocalDateTime inicio, LocalDateTime fim, String motivo) {
        this.id = id;
        this.inicio = inicio;
        this.fim = fim;
        this.motivo = motivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
