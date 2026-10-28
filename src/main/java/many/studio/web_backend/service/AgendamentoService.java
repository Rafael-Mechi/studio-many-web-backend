package many.studio.web_backend.service;

import many.studio.web_backend.mapper.PacoteMapper;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoRequest;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.dto.agendamento.CancelarAgendamentoRequest;
import many.studio.web_backend.dto.agendamento.ResumoAgendamento;
import many.studio.web_backend.dto.usuario.UsuarioDetalhesDto;
import many.studio.web_backend.dto.usuario.VisaoGeralClienteResponse;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.agendamento.AgendamentoMapper;
import many.studio.web_backend.exception.NonAuthorizedException;
import many.studio.web_backend.repository.*;
import many.studio.web_backend.service.helper.AgendamentoHelper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    private final PerfilRepository perfilRepository;
    private final NotificacaoAgendamentoService notificacaoAgendamentoService;
    private final PagamentoRepository pagamentoRepository;

    private final AgendamentoHelper agendamentoHelper;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AgendamentoItemRepository agendamentoItemRepository,
            ClienteRepository clienteRepository,
            StatusAgendamentoRepository statusAgendamentoRepository, PacoteRepository pacoteRepository,
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository,
            UsuarioRepository usuarioRepository, PerfilRepository perfilRepository,
            NotificacaoAgendamentoService notificacaoAgendamentoService,
            PagamentoRepository pagamentoRepository, AgendamentoHelper agendamentoHelper
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
        this.notificacaoAgendamentoService = notificacaoAgendamentoService;
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
                StatusAgendamento.AGENDADO,
                StatusAgendamento.SOLICITAR_CONFIRMACAO,
                StatusAgendamento.REAGENDADO,
                StatusAgendamento.CHECK_IN,
                StatusAgendamento.EM_ATENDIMENTO
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

        Long usuarioIdFiltro = role.equals("ROLE_PROFISSIONAL") ? id : null;

        List<VisaoGeralClienteResponse> respostas = new ArrayList<>();

        for (Cliente cliente : clientes) {

            Long clienteId = cliente.getId();

            Integer qtdNoShows =
                    agendamentoRepository
                            .findByClienteIdAndStatusAgendamentoEstado(
                                    clienteId,
                                    StatusAgendamento.NO_SHOW,
                                    usuarioIdFiltro
                            )
                            .size();

            List<Pagamento> pagamentosPagos =
                    pagamentoRepository
                            .findByAgendamentoClienteIdAndStatusPagamentoEstado(
                                    clienteId,
                                    "pago",
                                    usuarioIdFiltro
                            );

            Double somaPagamentos = pagamentosPagos
                    .stream()
                    .mapToDouble(Pagamento::getValor)
                    .sum();

            Integer qtdAgendamentosPendentes =
                    agendamentoRepository
                            .countAgendamentosPendentes(
                                    clienteId,
                                    statusPendentes,
                                    usuarioIdFiltro
                            )
                            .intValue();

            List<ResumoAgendamento> resumoAgendamentos =
                    agendamentoRepository
                            .buscarResumoCliente(clienteId, usuarioIdFiltro);

            VisaoGeralClienteResponse response =
                    new VisaoGeralClienteResponse();

            response.setNomeUsuario(cliente.getNome());

            response.setEmailUsuario(
                    cliente.getUsuario().getEmail()
            );

            response.setTelefoneUsuario(cliente.getTelefone());

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


    @Transactional
    public List<AgendamentoCriacaoResponse> criar(Long id, String role, List<AgendamentoCriacaoRequest> request) {
        List<Agendamento> agendamentosCriados = new ArrayList<>();

        for(AgendamentoCriacaoRequest agendamentoRequest : request) {
            agendamentoHelper.validarIntegridadeUsuario(id, role, agendamentoRequest.getClienteId());

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
                    .findByEstado(StatusAgendamento.AGUARDANDO_SINAL)
                    .orElseThrow(() -> new EntityNotFoundException("Status não existe"));

            Agendamento agendamento = new Agendamento();
            agendamento.setCliente(cliente);
            agendamento.setPacote(pacote);
            agendamento.setStatusAgendamento(status);
            agendamento.setProfissional(profissional);
            agendamento.setCriadoPorUsuario(usuario);
            agendamento.setPreco(PacoteMapper.precoEfetivo(pacote));
            agendamento.setPrecoFinal(PacoteMapper.precoEfetivo(pacote));

            Agendamento saved = agendamentoRepository.save(agendamento);
            List<AgendamentoItem> itens = criarItens(saved, agendamentoRequest.getHorario());
            List<AgendamentoItem> savedList = agendamentoItemRepository.saveAll(itens);
            saved.setItens(savedList);
            agendamentosCriados.add(saved);

            notificacaoAgendamentoService.notificarNovaSolicitacao(saved);
        }

        return AgendamentoMapper.toResponseList(agendamentosCriados);
    }

    @Transactional
    public void confirmar(Long idAgendamento, UsuarioDetalhesDto usuarioLogado) {
        Agendamento agendamento = buscarPorId(idAgendamento);
        validarPermissaoOperacional(agendamento, usuarioLogado, "confirmar agendamentos");
        validarStatusEditavel(agendamento);

        aplicarStatus(agendamento, StatusAgendamento.AGENDADO);
    }

    @Transactional
    public void fazerCheckIn(Long idAgendamento, UsuarioDetalhesDto usuarioLogado) {
        Agendamento agendamento = buscarPorId(idAgendamento);
        validarPermissaoOperacional(agendamento, usuarioLogado, "fazer check-in nos agendamentos");
        validarStatusEditavel(agendamento);

        aplicarStatus(agendamento, StatusAgendamento.CHECK_IN);
    }

    @Transactional
    public void concluir(Long idAgendamento, UsuarioDetalhesDto usuarioLogado) {
        Agendamento agendamento = buscarPorId(idAgendamento);
        validarPermissaoOperacional(agendamento, usuarioLogado, "concluir agendamentos");
        validarStatusEditavel(agendamento);

        aplicarStatus(agendamento, StatusAgendamento.CONCLUIDO);
    }

    @Transactional
    public void emAtendimento(Long idAgendamento, UsuarioDetalhesDto usuarioLogado) {
        Agendamento agendamento = buscarPorId(idAgendamento);
        validarPermissaoOperacional(agendamento, usuarioLogado, "deixar agendamentos em atendimento");
        validarStatusEditavel(agendamento);

        aplicarStatus(agendamento, StatusAgendamento.EM_ATENDIMENTO);
    }

    @Transactional
    public void marcarNoShow(Long idAgendamento, UsuarioDetalhesDto usuarioLogado) {
        Agendamento agendamento = buscarPorId(idAgendamento);
        validarPermissaoOperacional(agendamento, usuarioLogado, "marcar no-show em agendamentos");
        validarStatusEditavel(agendamento);

        String estadoAtual = estadoDoAgendamento(agendamento);
        Set<String> estadosPermitidos = Set.of(
                StatusAgendamento.AGENDADO,
                StatusAgendamento.REAGENDADO
        );

        if (!estadosPermitidos.contains(estadoAtual)) {
            throw new EntityConflictException(
                    "O no-show só pode ser registrado em agendamentos confirmados"
            );
        }

        aplicarStatus(agendamento, StatusAgendamento.NO_SHOW);
    }

    @Transactional
    public void recusar(Long idAgendamento, UsuarioDetalhesDto usuarioLogado) {
        Agendamento agendamento = buscarPorId(idAgendamento);
        validarPermissaoOperacional(agendamento, usuarioLogado, "recusar agendamentos");
        validarStatusEditavel(agendamento);

        String estadoAtual = estadoDoAgendamento(agendamento);
        Set<String> estadosPermitidos = Set.of(
                StatusAgendamento.AGUARDANDO_SINAL,
                StatusAgendamento.SOLICITAR_CONFIRMACAO
        );

        if (!estadosPermitidos.contains(estadoAtual)) {
            throw new EntityConflictException(
                    "Somente solicitações aguardando análise podem ser recusadas"
            );
        }

        aplicarStatus(agendamento, StatusAgendamento.RECUSADO);
    }

    private void aplicarStatus(Agendamento agendamento, String estado) {
        StatusAgendamento status = statusAgendamentoRepository
                .findByEstado(estado)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Status " + estado.toUpperCase() + " não encontrado"
                ));

        agendamento.setStatusAgendamento(status);
        agendamentoRepository.save(agendamento);
        notificacaoAgendamentoService.notificarStatus(agendamento);
    }

    private void validarPermissaoOperacional(
            Agendamento agendamento,
            UsuarioDetalhesDto usuarioLogado,
            String acao
    ) {
        boolean admin = temPerfil(usuarioLogado, "ROLE_ADMIN");
        boolean profissional = temPerfil(usuarioLogado, "ROLE_PROFISSIONAL");

        if (!admin && !profissional) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Apenas administradores e profissionais podem " + acao
            );
        }

        if (admin) {
            return;
        }

        Long usuarioDoProfissional = agendamento.getProfissional() == null
                || agendamento.getProfissional().getUsuario() == null
                ? null
                : agendamento.getProfissional().getUsuario().getId();

        if (!usuarioLogado.getId().equals(usuarioDoProfissional)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não pode alterar agendamentos de outro profissional"
            );
        }
    }

    private boolean temPerfil(UsuarioDetalhesDto usuario, String perfil) {
        return usuario.getAuthorities()
                .stream()
                .anyMatch(authority -> perfil.equals(authority.getAuthority()));
    }

    private void validarStatusEditavel(Agendamento agendamento) {
        String estado = estadoDoAgendamento(agendamento);
        if (Set.of(
                StatusAgendamento.CONCLUIDO,
                StatusAgendamento.CANCELADO,
                StatusAgendamento.RECUSADO,
                StatusAgendamento.NO_SHOW
        ).contains(estado)) {
            throw new EntityConflictException(
                    "O agendamento já está com o status '" + estado + "' e não pode ser alterado"
            );
        }
    }

    private String estadoDoAgendamento(Agendamento agendamento) {
        if (agendamento.getStatusAgendamento() == null
                || agendamento.getStatusAgendamento().getEstado() == null) {
            return "";
        }
        return agendamento.getStatusAgendamento().getEstado().trim().toLowerCase();
    }


    @Transactional
    public void cancelarAgendamento(Long idAgendamento, CancelarAgendamentoRequest requestDto, Long idUsuario){
        if(!(agendamentoHelper.isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))){
            throw new NonAuthorizedException("Erro ao cancelar agendamento");
        }

        Agendamento agendamento = buscarPorId(idAgendamento);
        validarStatusEditavel(agendamento);

        List<AgendamentoItem> itens = agendamentoItemRepository.findByAgendamentoId(idAgendamento);
        LocalDateTime inicioAgendamento = itens.stream()
                .map(AgendamentoItem::getInicioAtendimento)
                .filter(dataHora -> dataHora != null)
                .min(LocalDateTime::compareTo)
                .orElseThrow(() -> new EntityConflictException(
                        "O agendamento não possui sessões cadastradas"
                ));

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        boolean admin = usuario.getPerfil() != null
                && "ROLE_ADMIN".equals(usuario.getPerfil().getPerfil());

        if(inicioAgendamento.isBefore(LocalDateTime.now().plusHours(24)) && !admin){
            throw new NonAuthorizedException("Não é possível cancelar agendamento com menos de 24 horas");
        }

        StatusAgendamento statusCancelado = statusAgendamentoRepository
                .findByEstado(StatusAgendamento.CANCELADO)
                .orElseThrow(() ->
                        new EntityNotFoundException("Status cancelado não encontrado"));

        agendamento.setStatusAgendamento(statusCancelado);
        agendamento.setCanceladoEm(LocalDateTime.now());
        agendamento.setCancelamentoMotivo(requestDto.getMotivo());

        agendamentoRepository.save(agendamento);
        notificacaoAgendamentoService.notificarStatus(agendamento);
    }

    public List<AgendamentoItem> criarItens(
            Agendamento agendamento,
            LocalDateTime horarioAgendado
    ) {

        return IntStream
                .rangeClosed(
                        0,
                        agendamento.getPacote().getTotalSessoes() - 1
                )
                .mapToObj(sessao -> {

                    AgendamentoItem item = new AgendamentoItem();

                    item.setInicioAtendimento(
                            horarioAgendado.plusDays(sessao * 7L)
                    );

                    item.setFimAtendimento(
                            item.getInicioAtendimento()
                                    .plusMinutes(
                                            agendamento.getPacote()
                                                    .getServico()
                                                    .getDuracaoMinutos()
                                    )
                    );

                    item.setAgendamento(agendamento);

                    item.setServico(
                            agendamento.getPacote().getServico()
                    );

                    item.setProfissional(
                            agendamento.getProfissional()
                    );

                    return item;
                })
                .toList();
    }
}