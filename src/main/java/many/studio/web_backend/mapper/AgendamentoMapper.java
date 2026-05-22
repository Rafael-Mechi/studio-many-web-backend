package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.dto.agendamento.AgendamentoItemResponse;
import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.AgendamentoItem;

import java.util.List;

public class AgendamentoMapper {

    public static AgendamentoCriacaoResponse toResponse(Agendamento agendamento, List<AgendamentoItem> itens) {
        AgendamentoCriacaoResponse.ClienteResponse cliente = new AgendamentoCriacaoResponse.ClienteResponse();
        cliente.setId(agendamento.getCliente().getId());
        cliente.setNome(agendamento.getCliente().getNome());

        AgendamentoCriacaoResponse response = new AgendamentoCriacaoResponse();
        response.setId(agendamento.getId());
        response.setInicio(agendamento.getInicio());
        response.setFim(agendamento.getFim());
        response.setCriadoEm(agendamento.getCriadoEm());
        response.setStatus(agendamento.getStatusAgendamento());
        if (agendamento.getCriadoPorUsuario() != null) {
            response.setCriadoPorUsuarioId(agendamento.getCriadoPorUsuario().getId());
        }
        response.setCliente(cliente);
        response.setItens(AgendamentoItemMapper.toResponseList(itens));
        return response;
    }
}