package many.studio.web_backend.mapper.agendamento;

import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoRequest;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.entity.*;

import java.util.List;

public class AgendamentoMapper {

    public static Agendamento toEntity(AgendamentoCriacaoRequest request) {

        Cliente cliente = new Cliente();
        cliente.setId(request.getClienteId());

        Profissional profissional = new Profissional();
        profissional.setId(request.getProfissionalId());

        Pacote pacote = new Pacote();
        pacote.setId(request.getPacoteId());

        Usuario usuario = new Usuario();
        usuario.setId(request.getUsuarioCriadorId());

        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);
        agendamento.setPacote(pacote);
        agendamento.setCriadoPorUsuario(usuario);

        return agendamento;
    }

    public static AgendamentoCriacaoResponse toResponse(Agendamento agendamento) {

        return null;
    }


}