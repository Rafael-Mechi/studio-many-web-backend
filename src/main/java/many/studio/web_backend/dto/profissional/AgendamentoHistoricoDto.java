package many.studio.web_backend.dto.profissional;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record AgendamentoHistoricoDto(
        @Schema(description = "Nome do serviço realizado", example = "Depilação a laser")
        String servico,
        @Schema(description = "Data e hora do agendamento")
        LocalDateTime data,
        @Schema(description = "Nome do profissional que atendeu", example = "Joana")
        String nomeProfissional,
        @Schema(description = "Estado atual do agendamento (PENDENTE, CONCLUÍDO, etc.)", example = "CONCLUÍDO")
        String status
) {}
