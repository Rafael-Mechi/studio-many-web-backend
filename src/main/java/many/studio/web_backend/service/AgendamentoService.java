package many.studio.web_backend.service;

import jakarta.persistence.EntityNotFoundException;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.AgendamentoItem;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.entity.enums.StatusAgendamentoItem;
import many.studio.web_backend.mapper.AgendamentoMapper;
import many.studio.web_backend.repository.AgendamentoRepository;
import many.studio.web_backend.repository.ClienteRepository;
import many.studio.web_backend.repository.PacoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final PacoteRepository pacoteRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, ClienteRepository clienteRepository, PacoteRepository pacoteRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.pacoteRepository = pacoteRepository;
    }

    public AgendamentoCriacaoResponse criar(Agendamento agendamento, List<LocalDateTime> horarios) {

        clienteRepository.findById(agendamento.getCliente().getId())
                .orElseThrow(() -> new EntityNotFoundException("A"));

        pacoteRepository.findById(agendamento.getPacote().getId())
                .orElseThrow(() -> new EntityNotFoundException("B"));

        Profissional profissional = new Profissional();
        profissional.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(agendamento.getCliente().getId());

        agendamento.setProfissional(profissional);
        agendamento.setCriadoPorUsuario(usuario);

        Agendamento saved = agendamentoRepository.save(agendamento);
        List<AgendamentoItem> itens = criarItens(saved, horarios);

        return AgendamentoMapper.toResponse(saved, itens);
    }

    public List<AgendamentoItem> criarItens(Agendamento agendamento, List<LocalDateTime> horarios) {
        return IntStream
                .rangeClosed(0, agendamento.getPacote().getTotalSessoes() -1)
                .mapToObj(sessao -> {

                    AgendamentoItem item = new AgendamentoItem();
                    item.setDataHora(horarios.get(sessao));
                    item.setPrecoUnitario(agendamento.getPacote().getPrecoTotal() / agendamento.getPacote().getTotalSessoes());
                    item.setStatus(StatusAgendamentoItem.PENDENTE);
                    item.setAgendamento(agendamento);

                    return item;
                })
                .toList();
    }
}
