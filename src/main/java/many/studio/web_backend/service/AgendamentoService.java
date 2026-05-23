package many.studio.web_backend.service;

import jakarta.transaction.Transactional;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoRequest;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.dto.agendamento.AgendamentoItemCriacaoRequest;
import many.studio.web_backend.dto.agendamento.CancelarAgendamentoRequest;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.exception.NonAuthorizedException;
import many.studio.web_backend.mapper.AgendamentoMapper;
import many.studio.web_backend.repository.*;
import many.studio.web_backend.service.helper.AgendamentoHelper;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoItemRepository agendamentoItemRepository;
    private final ClienteRepository clienteRepository;
    private final StatusAgendamentoRepository statusAgendamentoRepository;
    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    private AgendamentoHelper agendamentoHelper;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AgendamentoItemRepository agendamentoItemRepository,
            ClienteRepository clienteRepository,
            StatusAgendamentoRepository statusAgendamentoRepository,
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository,
            UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, AgendamentoHelper agendamentoHelper
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoItemRepository = agendamentoItemRepository;
        this.clienteRepository = clienteRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.agendamentoHelper = agendamentoHelper;
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

    @Transactional
    public void cancelarAgendamento(Long idAgendamento, CancelarAgendamentoRequest requestDto, Long idUsuario){
        if(!(agendamentoHelper.isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))){
            throw new NonAuthorizedException("Erro ao cancelar agendamento");
        }

        if(!(agendamentoRepository.existsById(idAgendamento))){
            throw new EntityNotFoundException("Agendamento não encontrado");
        }

        Optional<Agendamento> agendamento = agendamentoRepository.findById(idAgendamento);
        LocalDateTime inicioAgendamento = agendamento.get().getInicio();

        Long perfilUsuario = perfilRepository.findByUsuarioId(idUsuario).getId();

        if(inicioAgendamento.isBefore(LocalDateTime.now().plusHours(24)) && perfilUsuario != 1){
            throw new NonAuthorizedException("Não é possível cancelar agendamento com menos de 24 horas");
        }

        agendamentoItemRepository.deleteByAgendamentoId(idAgendamento);

        StatusAgendamento statusCancelado = statusAgendamentoRepository
                .findByEstado("cancelado")
                .orElseThrow(() ->
                        new RuntimeException("Status cancelado não encontrado"));

        agendamento.get().setStatusAgendamento(statusCancelado);
        agendamento.get().setCanceladoEm(LocalDateTime.now());
        agendamento.get().setCancelamentoMotivo(requestDto.getMotivo());

        agendamentoRepository.save(agendamento.get());
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