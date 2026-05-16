package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoRequest;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Pacote;

import java.util.List;

public class AgendamentoMapper {

    public static Agendamento toEntity(AgendamentoCriacaoRequest request) {

        Cliente cliente = new Cliente();
        cliente.setId(request.getClienteId());

        Pacote pacote = new Pacote();
        pacote.setId(request.getPacoteId());

        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setPacote(pacote);

        return agendamento;
    }

    public static AgendamentoCriacaoResponse toResponse(Agendamento agendamento, List<AgendamentoCriacaoResponse.AgendamentoItemResponse> itens) {

        AgendamentoCriacaoResponse.ClienteResponse cliente = new AgendamentoCriacaoResponse.ClienteResponse();
        cliente.setId(agendamento.getCliente().getId());
        cliente.setNome(agendamento.getCliente().getNome());

        AgendamentoCriacaoResponse.ProfissionalResponse profissional = new AgendamentoCriacaoResponse.ProfissionalResponse();
        profissional.setId(agendamento.getProfissional().getId());
        profissional.setNome(agendamento.getProfissional().getNome());

        AgendamentoCriacaoResponse.PacoteResponse pacote = new AgendamentoCriacaoResponse.PacoteResponse();
        pacote.setId(agendamento.getPacote().getId());
        pacote.setNome(agendamento.getPacote().getNome());
        pacote.setTotalSessoes(agendamento.getPacote().getTotalSessoes());
        pacote.setPrecoTotal(agendamento.getPacote().getPrecoTotal());
        pacote.setValidadeDias(agendamento.getPacote().getValidadeDias());
        pacote.setAtivo(agendamento.getPacote().getAtivo());

        AgendamentoCriacaoResponse response = new AgendamentoCriacaoResponse();
        response.setId(agendamento.getId());
        response.setCriadoEm(agendamento.getCriadoEm());
        response.setStatus(agendamento.getStatusAgendamento());
        response.setCliente(cliente);
        response.setProfissional(profissional);
        response.setPacote(pacote);
        response.setItens(itens);

        return response;
    }
}
