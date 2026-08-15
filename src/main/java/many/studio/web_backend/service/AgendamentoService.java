package many.studio.web_backend.service;

import jakarta.transaction.Transactional;
import many.studio.web_backend.dto.agendamento.*;
import many.studio.web_backend.config.twilio.WhatsAppService;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoRequest;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.dto.agendamento.AgendamentoItemCriacaoRequest;
import many.studio.web_backend.dto.agendamento.CancelarAgendamentoRequest;
import many.studio.web_backend.dto.usuario.UsuarioDetalhesDto;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.agendamento.AgendamentoItemMapper;
import many.studio.web_backend.mapper.agendamento.AgendamentoMapper;
import many.studio.web_backend.exception.NonAuthorizedException;
import many.studio.web_backend.mapper.agendamento.AgendamentoMapper;
import many.studio.web_backend.repository.*;
import many.studio.web_backend.service.helper.AgendamentoHelper;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.Optional;

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
    private final PerfilRepository perfilRepository;
    private final WhatsAppService whatsAppService;

    private final AgendamentoHelper agendamentoHelper;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AgendamentoItemRepository agendamentoItemRepository,
            ClienteRepository clienteRepository,
            StatusAgendamentoRepository statusAgendamentoRepository, PacoteRepository pacoteRepository,
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository,
            UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, WhatsAppService whatsAppService, AgendamentoHelper agendamentoHelper
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoItemRepository = agendamentoItemRepository;
        this.clienteRepository = clienteRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
        this.pacoteRepository = pacoteRepository;
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.whatsAppService = whatsAppService;
        this.agendamentoHelper = agendamentoHelper;
    }

    public List<Agendamento> buscarTodos() {
        return agendamentoRepository.findAll();
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
    }

    public List<Agendamento> buscarAgendamentos(Long id, String role) {
        if(role.equalsIgnoreCase("ROLE_ADMIN")){
            return agendamentoRepository.findAll();
        }
        else if(role.equalsIgnoreCase("ROLE_PROFISSIONAL")){
            return agendamentoRepository.findByProfissionalUsuarioId(id);
        }
        return agendamentoRepository.findByUsuarioId(id);
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

        StatusAgendamento status = statusAgendamentoRepository
                .findByEstado("solicitar confirmacao agendamento")
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
        saved.setItens(savedList);

        try {

            String mensagem = """
                    NOVA SOLICITAÇÃO DE AGENDAMENTO
                    
                    Cliente: %s
                    
                    Pacote: %s
                    Serviço: %s
                    Sessões: %d

                    Valor Final: R$ %.2f
                    
                    Data/Hora: %s
                    
                    Responda:
                    1 - Confirmar
                    2 - Recusar
                    """
                    .formatted(
                            cliente.getNome(),
                            pacote.getNome(),
                            pacote.getServico().getNome(),
                            pacote.getTotalSessoes(),
                            saved.getPrecoFinal(),
                            horarioAgendado
                    );

            whatsAppService.enviarMensagem(
                    profissional.getTelefone(),
                    mensagem
            );

        } catch (Exception e) {
            System.out.println("Erro ao enviar mensagem: " + e.fillInStackTrace());
        }

        return AgendamentoMapper.toResponse(saved);
    }

    public void confirmar(Long idAgendamento, UsuarioDetalhesDto usuarioLogado) {

        Agendamento agendamento = buscarPorId(idAgendamento);

        boolean admin = usuarioLogado.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!admin) {

            boolean funcionario = usuarioLogado.getAuthorities()
                    .stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_FUNCIONARIO"));

            if (!funcionario) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Apenas administradores e funcionários podem confirmar agendamentos"
                );
            }

            Long usuarioDoProfissional =
                    agendamento.getProfissional()
                            .getUsuario()
                            .getId();

            if (!usuarioDoProfissional.equals(usuarioLogado.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Você não pode confirmar agendamentos de outro profissional"
                );
            }
        }

        StatusAgendamento statusConfirmado =
                statusAgendamentoRepository
                        .findByEstado("agendado")
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Status AGENDADO não encontrado"
                                ));

        agendamento.setStatusAgendamento(statusConfirmado);

        agendamentoRepository.save(agendamento);
    }


    @Transactional
    public void cancelarAgendamento(Long idAgendamento, CancelarAgendamentoRequest requestDto, Long idUsuario){
        if(!(agendamentoHelper.isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))){
            throw new NonAuthorizedException("Erro ao cancelar agendamento");
        }

        if(!(agendamentoRepository.existsById(idAgendamento))){
            throw new EntityNotFoundException("Agendamento não encontrado");
        }

        List<AgendamentoItem> itens = agendamentoItemRepository.findByAgendamentoId(idAgendamento);
        LocalDateTime inicioAgendamento = itens.get(0).getInicioAtendimento();
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Long perfilUsuario = usuario.getPerfil().getId();

        if(inicioAgendamento.isBefore(LocalDateTime.now().plusHours(24)) && perfilUsuario != 1L){
            throw new NonAuthorizedException("Não é possível cancelar agendamento com menos de 24 horas");
        }

        agendamentoItemRepository.deleteByAgendamentoId(idAgendamento);

        StatusAgendamento statusCancelado = statusAgendamentoRepository
                .findByEstado("cancelado")
                .orElseThrow(() ->
                        new RuntimeException("Status cancelado não encontrado"));

        Optional<Agendamento> agendamento = agendamentoRepository.findById(idAgendamento);
        agendamento.get().setStatusAgendamento(statusCancelado);
        agendamento.get().setCanceladoEm(LocalDateTime.now());
        agendamento.get().setCancelamentoMotivo(requestDto.getMotivo());

        agendamentoRepository.save(agendamento.get());
    }

    public List<AgendamentoItem> criarItens(Agendamento agendamento, LocalDateTime horaraioAgendado) {

        List<AgendamentoItem> agendamentosCliente = agendamentoItemRepository.findByClienteId(agendamento.getCliente().getId());
        List<AgendamentoItem> agendamentosProfissional = agendamentoItemRepository.findByProfissionalId(agendamento.getProfissional().getId());

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