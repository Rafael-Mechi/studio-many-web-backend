package many.studio.web_backend.dto.config;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class HorarioServicoDto {

    @NotNull(message = "Serviço é obrigatório")
    private Long servicoId;

    private String nome;

    @NotNull(message = "Início é obrigatório")
    private LocalTime inicio;

    @NotNull(message = "Fim é obrigatório")
    private LocalTime fim;

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
