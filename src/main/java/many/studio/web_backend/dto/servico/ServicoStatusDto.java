package many.studio.web_backend.dto.servico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ServicoStatusDto {

    @NotNull
    @Schema(description = "Disponibilidade do serviço para agendamento", example = "true")
    private Boolean ativo;

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
