package many.studio.web_backend.mapper.config;

import many.studio.web_backend.dto.config.AgendamentoAfetadoDto;
import many.studio.web_backend.dto.config.BloqueioCriacaoResponse;
import many.studio.web_backend.dto.config.BloqueioResponse;
import many.studio.web_backend.entity.AgendamentoItem;
import many.studio.web_backend.entity.Bloqueio;

import java.util.List;

public class BloqueioMapper {

    public static BloqueioResponse toResponse(Bloqueio bloqueio) {
        return new BloqueioResponse(
                bloqueio.getId(), bloqueio.getInicio(), bloqueio.getFim(), bloqueio.getMotivo());
    }

    public static List<BloqueioResponse> toResponseList(List<Bloqueio> bloqueios) {
        return bloqueios.stream().map(BloqueioMapper::toResponse).toList();
    }

    public static BloqueioCriacaoResponse toCriacaoResponse(Bloqueio bloqueio,
                                                            List<AgendamentoAfetadoDto> afetados) {
        return new BloqueioCriacaoResponse(toResponse(bloqueio), afetados);
    }

    public static AgendamentoAfetadoDto toAgendamentoAfetadoDto(AgendamentoItem item) {
        String clienteNome = item.getAgendamento().getCliente() != null
                ? item.getAgendamento().getCliente().getNome()
                : null;
        return new AgendamentoAfetadoDto(
                item.getAgendamento().getId(),
                item.getInicioAtendimento(),
                clienteNome);
    }
}
