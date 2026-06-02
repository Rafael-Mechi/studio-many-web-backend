package many.studio.web_backend.mapper.agendamento;

import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoRequest;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.dto.agendamento.AgendamentoItemResponse;
import many.studio.web_backend.dto.agendamento.AgendamentoResponse;
import many.studio.web_backend.entity.*;

import java.util.List;

public class AgendamentoMapper {

    public static AgendamentoCriacaoResponse toResponse(Agendamento agendamento) {

        List<AgendamentoItemResponse> itensResponse = AgendamentoItemMapper.toResponseList(agendamento.getItens());

        AgendamentoCriacaoResponse.ClienteDto cliente = new AgendamentoCriacaoResponse.ClienteDto();
        cliente.setId(agendamento.getCliente().getId());
        cliente.setNome(agendamento.getCliente().getNome());

        AgendamentoCriacaoResponse.ProfissionalDto profissional = new AgendamentoCriacaoResponse.ProfissionalDto();
        profissional.setId(agendamento.getProfissional().getId());
        profissional.setNome(agendamento.getProfissional().getNome());

        AgendamentoCriacaoResponse.UsuarioDto usuario = new AgendamentoCriacaoResponse.UsuarioDto();
        usuario.setId(agendamento.getCriadoPorUsuario().getId());
        usuario.setEmail(agendamento.getCriadoPorUsuario().getEmail());

        AgendamentoCriacaoResponse.PacoteDto.ServicoDto servico = new AgendamentoCriacaoResponse.PacoteDto.ServicoDto();
        servico.setId(agendamento.getPacote().getServico().getId());
        servico.setNome(agendamento.getPacote().getServico().getNome());

        AgendamentoCriacaoResponse.PacoteDto pacote = new AgendamentoCriacaoResponse.PacoteDto();
        pacote.setId(agendamento.getPacote().getId());
        pacote.setNome(agendamento.getPacote().getNome());
        pacote.setTotalSessoes(agendamento.getPacote().getTotalSessoes());
        pacote.setValidadeDias(agendamento.getPacote().getTotalSessoes());
        pacote.setServico(servico);


        AgendamentoCriacaoResponse response = new AgendamentoCriacaoResponse();
        response.setId(agendamento.getId());
        response.setCriadoEm(agendamento.getCriadoEm());
        response.setPreco(agendamento.getPreco());
        response.setDescontoPorcentagem(agendamento.getDescontoPorcentagem());
        response.setPrecoFinal(agendamento.getPrecoFinal());
        response.setStatus(agendamento.getStatusAgendamento().getEstado());
        response.setItens(itensResponse);
        response.setCliente(cliente);
        response.setProfissional(profissional);
        response.setCriadoPor(usuario);
        response.setPacote(pacote);

        return response;
    }

    public static AgendamentoResponse toAgendamentoResponse(Agendamento agendamento) {
        Pacote pacote = agendamento.getPacote();

        Profissional profissional = agendamento.getProfissional();

        Cliente cliente = agendamento.getCliente();

        AgendamentoResponse.PacoteResponse pacoteResponse = new AgendamentoResponse.PacoteResponse();
        pacoteResponse.setId(pacote.getId());
        pacoteResponse.setNome(pacote.getNome());
        pacoteResponse.setAtivo(pacote.getAtivo());
        pacoteResponse.setTotalSessoes(pacote.getTotalSessoes());

        AgendamentoResponse.ProfissionalResponse profissionalResponse = new AgendamentoResponse.ProfissionalResponse();
        profissionalResponse.setId(profissional.getId());
        profissionalResponse.setNome(profissional.getNome());

        AgendamentoResponse.ClienteResponse clienteResponse = new AgendamentoResponse.ClienteResponse();
        clienteResponse.setId(cliente.getId());
        clienteResponse.setNome(cliente.getNome());

        List<AgendamentoItem> itens = agendamento.getItens();

        List<AgendamentoItemResponse> itensResponse = itens.stream().map(AgendamentoItemMapper::toResponse).toList();

        AgendamentoResponse response = new AgendamentoResponse();
        response.setId(agendamento.getId());
        response.setStatus(agendamento.getStatusAgendamento());
        response.setCliente(clienteResponse);
        response.setPacote(pacoteResponse);
        response.setProfissional(profissionalResponse);
        response.setItens(itensResponse);

        return response;
    }

    public static List<AgendamentoResponse> toAgendamentoResponseList(List<Agendamento> agendamentos) {
        return agendamentos.stream()
                .map(AgendamentoMapper::toAgendamentoResponse)
                .toList();
    }

}