package many.studio.web_backend.dto.profissional;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public record ClienteDetalheDto(
        @Schema(description = "ID do cliente", example = "1")
        Long id,
        @Schema(description = "Nome do cliente", example = "Giovana Lopes")
        String nome,
        @Schema(description = "Telefone do cliente", example = "(11) 98765-4321")
        String telefone,
        @Schema(description = "Documento/CPF", example = "123.456.789-01")
        String documento,
        @Schema(description = "E-mail cadastrado", example = "giovana.lopes@gmail.com")
        String email,
        @Schema(description = "Total de faltas acumuladas", example = "0")
        Integer totalNoShows,
        @Schema(description = "Data da última visita", example = "2026-02-16")
        LocalDateTime ultimaVisita,
        @Schema(description = "Total financeiro investido", example = "250.00")
        Double totalGasto,
        @Schema(description = "Lista com o histórico recente de agendamentos")
        List<AgendamentoHistoricoDto> historicoRecente
) {}