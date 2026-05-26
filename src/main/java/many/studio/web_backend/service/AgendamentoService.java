package many.studio.web_backend.service;

import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoRequest;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.dto.agendamento.AgendamentoItemCriacaoRequest;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.agendamento.AgendamentoMapper;
import many.studio.web_backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoItemRepository agendamentoItemRepository;
    private final ClienteRepository clienteRepository;
    private final StatusAgendamentoRepository statusAgendamentoRepository;
    private final PacoteRepository pacoteRepository;
    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final UsuarioRepository usuarioRepository;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AgendamentoItemRepository agendamentoItemRepository,
            ClienteRepository clienteRepository,
            StatusAgendamentoRepository statusAgendamentoRepository, PacoteRepository pacoteRepository,
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoItemRepository = agendamentoItemRepository;
        this.clienteRepository = clienteRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
        this.pacoteRepository = pacoteRepository;
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public AgendamentoCriacaoResponse criar(AgendamentoCriacaoRequest request, LocalDateTime horarioAgendado) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        Pacote pacote = pacoteRepository.findById(request.getPacoteId())
                .orElseThrow(() -> new EntityNotFoundException("Pacote não encontrado"));

        Profissional profissional = profissionalRepository.findById(request.getProfissionalId())
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        Usuario usuario = usuarioRepository.findById(request.getUsuarioCriadorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        StatusAgendamento status = statusAgendamentoRepository.findStatusAgendamentoByEstado("solicitar confirmacao agendamento")
                .orElseThrow(() -> new EntityNotFoundException("Status não existe"));

        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setPacote(pacote);
        agendamento.setStatusAgendamento(status);
        agendamento.setProfissional(profissional);
        agendamento.setCriadoPorUsuario(usuario);
        agendamento.setPreco(pacote.getPrecoTotal());
        agendamento.setPrecoFinal(pacote.getPrecoTotal());

        Agendamento saved = agendamentoRepository.save(agendamento);
        List<AgendamentoItem> itens = criarItens(saved, horarioAgendado);
        List<AgendamentoItem> savedList = agendamentoItemRepository.saveAll(itens);

        return AgendamentoMapper.toResponse(agendamento, savedList);
    }

    private List<AgendamentoItem> criarItens(Agendamento agendamento, LocalDateTime horaraioAgendado) {
        return IntStream
                .rangeClosed(0, agendamento.getPacote().getTotalSessoes() -1)
                .mapToObj(sessao -> {

                    AgendamentoItem item = new AgendamentoItem();
                    item.setInicioAtendimento(horaraioAgendado.plusDays(sessao * 7L));
                    item.setFimAtendimento(item.getInicioAtendimento().plusMinutes(agendamento.getPacote().getServico().getDuracaoMinutos()));
                    item.setAgendamento(agendamento);

                    return item;
                })
                .toList();

    }
}