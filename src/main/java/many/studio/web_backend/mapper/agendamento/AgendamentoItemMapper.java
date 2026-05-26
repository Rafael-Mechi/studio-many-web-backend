package many.studio.web_backend.mapper.agendamento;

import many.studio.web_backend.dto.agendamento.AgendamentoItemResponse;
import many.studio.web_backend.entity.AgendamentoItem;

import java.util.List;

public class AgendamentoItemMapper {

    public static List<AgendamentoItemResponse> toResponseList(List<AgendamentoItem> itens) {
        return itens.stream().map(AgendamentoItemMapper::toResponse).toList();
    }

    public static AgendamentoItemResponse toResponse(AgendamentoItem item) {

        AgendamentoItemResponse response = new AgendamentoItemResponse();
        response.setId(item.getId());
        response.setInicioAtendimento(item.getInicioAtendimento());
        response.setFimAtendimento(item.getFimAtendimento());

        return response;
    }
}