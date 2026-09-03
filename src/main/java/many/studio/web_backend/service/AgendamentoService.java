package many.studio.web_backend.service;

import jakarta.transaction.Transactional;
import many.studio.web_backend.config.twilio.WhatsAppService;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoRequest;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.dto.agendamento.CancelarAgendamentoRequest;
import many.studio.web_backend.dto.agendamento.ResumoAgendamento;
import many.studio.web_backend.dto.usuario.UsuarioDetalhesDto;
import many.studio.web_backend.dto.usuario.VisaoGeralClienteResponse;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.agendamento.AgendamentoMapper;
import many.studio.web_backend.exception.NonAuthorizedException;
import many.studio.web_backend.repository.*;
import many.studio.web_backend.service.helper.AgendamentoHelper;
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
    private final PagamentoRepository pagamentoRepository;

    private final AgendamentoHelper agendamentoHelper;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AgendamentoItemRepository agendamentoItemRepository,
            ClienteRepository clienteRepository,
            StatusAgendamentoRepository statusAgendamentoRepository, PacoteRepository pacoteRepository,
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository,
            UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, WhatsAppService whatsAppService, PagamentoRepository pagamentoRepository, AgendamentoHelper agendamentoHelper
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
        this.pagamentoRepository = pagamentoRepository;
        this.agendamentoHelper = agendamentoHelper;
    }

    public List<Agendamento> buscarTodos() {
        return agendamentoRepository.findAll();
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
    }

    public List<VisaoGeralClienteResponse> buscarAgendamentos(
            Long id,
            String role
    ) {

        List<String> statusPendentes = List.of(
                "agendado",
                "confirmado",
                "reagendado",
                "solicitar cancelamento",
                "solicitar reagendamento",
                "em atendimento"
        );

        List<Cliente> clientes;

        if (role.equals("ROLE_CLIENTE")) {

            Cliente cliente = clienteRepository
                    .findByUsuario_Id(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Cliente não encontrado"
                            )
                    );

            clientes = List.of(cliente);

        } else if (role.equals("ROLE_ADMIN")) {

            clientes = clienteRepository.findAll();

        } else if (role.equals("ROLE_PROFISSIONAL")) {

            clientes = clienteRepository
                    .findClientesByProfissionalUsuarioId(id);

        } else {

            throw new IllegalArgumentException(
                    "Role inválida: " + role
            );
        }

        List<VisaoGeralClienteResponse> respostas = new ArrayList<>();

        for (Cliente cliente : clientes) {

            Long clienteId = cliente.getId();

            Integer qtdNoShows =
                    agendamentoRepository
                            .findByClienteIdAndStatusAgendamentoEstado(
                                    clienteId,
                                    "faltou"
                            )
                            .size();

            List<Pagamento> pagamentosPagos =
                    pagamentoRepository
                            .findByAgendamentoClienteIdAndStatusPagamentoEstado(
                                    clienteId,
                                    "pago"
                            );

            Double somaPagamentos = pagamentosPagos
                    .stream()
                    .mapToDouble(Pagamento::getValor)
                    .sum();

            Integer qtdAgendamentosPendentes =
                    agendamentoRepository
                            .countAgendamentosPendentes(
                                    clienteId,
                                    statusPendentes
                            )
                            .intValue();

            List<ResumoAgendamento> resumoAgendamentos =
                    agendamentoRepository
                            .buscarResumoCliente(clienteId);

            VisaoGeralClienteResponse response =
                    new VisaoGeralClienteResponse();

            response.setNomeUsuario(cliente.getNome());

            response.setEmailUsuario(
                    cliente.getUsuario().getEmail()
            );

            response.setNoShow(qtdNoShows);

            response.setTotalGasto(somaPagamentos);

            response.setAtendimentosPendentes(
                    qtdAgendamentosPendentes
            );

            response.setResumoAgendamentos(
                    resumoAgendamentos
            );

            respostas.add(response);
        }

        return respostas;
    }


    public List<AgendamentoCriacaoResponse> criar(Long id, List<AgendamentoCriacaoRequest> request) {
        List<Agendamento> agendamentosCriados = new ArrayList<>();

        for(AgendamentoCriacaoRequest agendamentoRequest : request) {
            agendamentoHelper.validarIntegridadeUsuario(id, agendamentoRequest.getClienteId());

            if(!agendamentoHelper.isPacoteAtivo(agendamentoRequest.getPacoteId())){
                throw new EntityNotFoundException("Pacote não ativo");
            }

            agendamentoHelper.validarConflitoHorarioAgendamento(agendamentoRequest.getHorario(),
                    agendamentoRequest.getProfissionalId(), agendamentoRequest.getPacoteId());

            Cliente cliente = clienteRepository.findById(agendamentoRequest.getClienteId())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

            Pacote pacote = pacoteRepository.findById(agendamentoRequest.getPacoteId())
                    .orElseThrow(() -> new EntityNotFoundException("Pacote não encontrado"));

            Profissional profissional = profissionalRepository.findById(agendamentoRequest.getProfissionalId())
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

            StatusAgendamento status = statusAgendamentoRepository
                    .findByEstado("aguardando sinal")
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
            List<AgendamentoItem> itens = criarItens(saved, agendamentoRequest.getHorario());
            List<AgendamentoItem> savedList = agendamentoItemRepository.saveAll(itens);
            saved.setItens(savedList);
            agendamentosCriados.add(saved);


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
                                agendamentoRequest.getHorario()
                        );

                whatsAppService.enviarMensagem(
                        profissional.getTelefone(),
                        mensagem
                );

            } catch (Exception e) {
                System.out.println("Erro ao enviar mensagem: " + e.fillInStackTrace());
            }
        }

        return AgendamentoMapper.toResponseList(agendamentosCriados);
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