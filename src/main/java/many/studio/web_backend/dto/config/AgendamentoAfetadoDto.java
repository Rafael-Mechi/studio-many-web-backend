package many.studio.web_backend.dto.config;

import java.time.LocalDateTime;

public class AgendamentoAfetadoDto {

    private Long id;
    private LocalDateTime dataHora;
    private String clienteNome;

    public AgendamentoAfetadoDto() {
    }

    public AgendamentoAfetadoDto(Long id, LocalDateTime dataHora, String clienteNome) {
        this.id = id;
        this.dataHora = dataHora;
        this.clienteNome = clienteNome;
    }

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

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }
}
