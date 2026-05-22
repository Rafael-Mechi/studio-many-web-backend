package many.studio.web_backend.service;

import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoRequest;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.dto.agendamento.AgendamentoItemCriacaoRequest;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.AgendamentoMapper;
import many.studio.web_backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoItemRepository agendamentoItemRepository;
    private final ClienteRepository clienteRepository;
    private final StatusAgendamentoRepository statusAgendamentoRepository;
    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final UsuarioRepository usuarioRepository;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AgendamentoItemRepository agendamentoItemRepository,
            ClienteRepository clienteRepository,
            StatusAgendamentoRepository statusAgendamentoRepository,
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoItemRepository = agendamentoItemRepository;
        this.clienteRepository = clienteRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public AgendamentoCriacaoResponse criar(AgendamentoCriacaoRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        StatusAgendamento status = statusAgendamentoRepository.findById(request.getStatusAgendamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Status de agendamento não encontrado"));

        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setStatusAgendamento(status);
        agendamento.setInicio(request.getInicio());
        agendamento.setFim(request.getFim());

        if (request.getCriadoPorUsuarioId() != null) {
            Usuario criadoPor = usuarioRepository.findById(request.getCriadoPorUsuarioId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
            agendamento.setCriadoPorUsuario(criadoPor);
        }

        Agendamento saved = agendamentoRepository.save(agendamento);
        List<AgendamentoItem> itens = criarItens(saved, request.getItens());
        List<AgendamentoItem> savedItens = agendamentoItemRepository.saveAll(itens);

        return AgendamentoMapper.toResponse(saved, savedItens);
    }

    private List<AgendamentoItem> criarItens(Agendamento agendamento, List<AgendamentoItemCriacaoRequest> itensRequest) {
        List<AgendamentoItem> itens = new ArrayList<>();
        for (AgendamentoItemCriacaoRequest itemReq : itensRequest) {
            Servico servico = servicoRepository.findById(itemReq.getServicoId())
                    .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado"));

            Profissional profissional = profissionalRepository.findById(itemReq.getProfissionalId())
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

            AgendamentoItem item = new AgendamentoItem();
            item.setAgendamento(agendamento);
            item.setServico(servico);
            item.setProfissional(profissional);
            item.setInicioAtendimento(itemReq.getInicioAtendimento());
            item.setFimAtendimento(itemReq.getFimAtendimento());
            item.setPreco(itemReq.getPreco());
            item.setDescontoPorcentagem(itemReq.getDescontoPorcentagem());
            item.setPrecoFinal(itemReq.getPrecoFinal());
            itens.add(item);
        }
        return itens;
    }
}