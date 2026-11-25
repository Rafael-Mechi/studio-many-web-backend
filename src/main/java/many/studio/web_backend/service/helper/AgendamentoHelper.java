package many.studio.web_backend.service.helper;

import many.studio.web_backend.dto.agendamento.TotalGastoItem;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AgendamentoHelper {
    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoItemRepository agendamentoItemRepository;
    private final ClienteRepository clienteRepository;
    private final StatusAgendamentoRepository statusAgendamentoRepository;
    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final UsuarioRepository usuarioRepository;
    private final PacoteRepository pacoteRepository;
    private final PagamentoRepository pagamentoRepository;

    public AgendamentoHelper(AgendamentoRepository agendamentoRepository, AgendamentoItemRepository agendamentoItemRepository, ClienteRepository clienteRepository, StatusAgendamentoRepository statusAgendamentoRepository, ServicoRepository servicoRepository, ProfissionalRepository profissionalRepository, UsuarioRepository usuarioRepository, PacoteRepository pacoteRepository, PagamentoRepository pagamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoItemRepository = agendamentoItemRepository;
        this.clienteRepository = clienteRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
        this.usuarioRepository = usuarioRepository;
        this.pacoteRepository = pacoteRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    public Double calcularTotalGasto(Long clienteId, Long usuarioId) {
        List<TotalGastoItem> itens = agendamentoRepository.buscarItensTotalGasto(clienteId, usuarioId);

        Map<Long, Double> pagosPorAgendamento = pagamentoRepository
                .findByAgendamentoClienteIdAndStatusPagamentoEstado(clienteId, "pago", usuarioId)
                .stream()
                .filter(pg -> pg.getAgendamento() != null && pg.getAgendamento().getId() != null)
                .filter(pg -> pg.getValor() != null)
                .collect(Collectors.groupingBy(
                        pg -> pg.getAgendamento().getId(),
                        Collectors.summingDouble(Pagamento::getValor)
                ));

        Map<Long, List<TotalGastoItem>> itensPorAgendamento = itens.stream()
                .collect(Collectors.groupingBy(TotalGastoItem::getAgendamentoId));

        double total = 0;
        for (Map.Entry<Long, List<TotalGastoItem>> entry : itensPorAgendamento.entrySet()) {
            List<TotalGastoItem> itensAgendamento = entry.getValue();
            String estado = itensAgendamento.get(0).getEstado();

            boolean atendido = StatusAgendamento.CONCLUIDO.equalsIgnoreCase(estado)
                    || StatusAgendamento.EM_ATENDIMENTO.equalsIgnoreCase(estado);
            boolean cancelado = StatusAgendamento.CANCELADO.equalsIgnoreCase(estado);

            if (atendido) {
                total += itensAgendamento.stream()
                        .mapToDouble(i -> i.getPreco() == null ? 0 : i.getPreco())
                        .sum();
            } else if (!cancelado) {
                total += pagosPorAgendamento.getOrDefault(entry.getKey(), 0.0);
            }
        }

        return total;
    }

    public Boolean isUsuarioValidoParaCancelamentoDeAgendamento(Long idUsuario, Long idAgendamento){
        Optional<Agendamento> a = agendamentoRepository.findById(idAgendamento);
        Optional<Usuario> u = usuarioRepository.findById(idUsuario);

        if(a.isEmpty() || u.isEmpty()) {
            return false;
        }

        Long fkUsuarioAgendamento = a.get().getCliente().getUsuario().getId();
        Long perfilUsuario = u.get().getPerfil().getId();

        return Objects.equals(fkUsuarioAgendamento, idUsuario) || perfilUsuario.equals(1L);
    }

    public boolean isUsuarioValidoParaAlterarAgendamento(Long idUsuario, Agendamento agendamento) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isEmpty() || agendamento == null) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        Long perfilUsuario = usuario.getPerfil() == null ? null : usuario.getPerfil().getId();

        if (perfilUsuario != null && perfilUsuario.equals(1L)) {
            return true;
        }

        boolean clienteDoAgendamento = agendamento.getCliente() != null
                && agendamento.getCliente().getUsuario() != null
                && Objects.equals(agendamento.getCliente().getUsuario().getId(), idUsuario);

        boolean profissionalDoAgendamento = agendamento.getProfissional() != null
                && agendamento.getProfissional().getUsuario() != null
                && Objects.equals(agendamento.getProfissional().getUsuario().getId(), idUsuario);

        return clienteDoAgendamento || profissionalDoAgendamento;
    }

    public void validarIntegridadeUsuario(Long id, String role, Long clienteId) {

        if ("ROLE_CLIENTE".equals(role)) {
            Cliente cliente = clienteRepository
                    .findByUsuario_Id(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Cliente não encontrado"
                            )
                    );

            if (!cliente.getId().equals(clienteId)) {
                throw new EntityNotFoundException(
                        "Cliente não encontrado"
                );
            }
        }
    }

    public Boolean isPacoteAtivo(Long pacoteId) {
        Optional<Pacote> p = pacoteRepository.findById(pacoteId);
        Pacote pacote = p.get();

        return pacote.getAtivo();
    }

    public void validarConflitoHorarioAgendamento(LocalDateTime horario, Long idProfissional, Long pacoteId) {
        Pacote pacote = pacoteRepository.findById(pacoteId)
                .orElseThrow(() -> new EntityNotFoundException("Pacote não encontrado"));

        Servico servico = pacote.getServico();
        int totalSessoes = pacote.getTotalSessoes() == null ? 1 : pacote.getTotalSessoes();

        for (int sessao = 0; sessao < totalSessoes; sessao++) {
            LocalDateTime inicio = horario.plusDays(sessao * 7L);
            validarConflitoHorario(
                    inicio,
                    inicio.plusMinutes(servico.getDuracaoMinutos()),
                    idProfissional,
                    null
            );
        }
    }

    public void validarConflitoHorarioItem(
            LocalDateTime inicio,
            Integer duracaoMinutos,
            Long idProfissional,
            Long idAgendamentoIgnorado
    ) {
        validarConflitoHorario(
                inicio,
                inicio.plusMinutes(duracaoMinutos),
                idProfissional,
                idAgendamentoIgnorado
        );
    }

    private void validarConflitoHorario(
            LocalDateTime novoInicio,
            LocalDateTime novoFim,
            Long idProfissional,
            Long idAgendamentoIgnorado
    ) {
        List<Agendamento> agendamentos = agendamentoRepository.findByProfissionalId(idProfissional);

        for (Agendamento agendamento : agendamentos) {
            if (idAgendamentoIgnorado != null
                    && idAgendamentoIgnorado.equals(agendamento.getId())) {
                continue;
            }

            String status = agendamento.getStatusAgendamento() == null
                    ? null
                    : agendamento.getStatusAgendamento().getEstado();

            boolean statusEncerrado = status != null && Set.of(
                    StatusAgendamento.CANCELADO,
                    StatusAgendamento.CONCLUIDO,
                    StatusAgendamento.RECUSADO,
                    StatusAgendamento.NO_SHOW
            ).contains(status.trim().toLowerCase());

            if (statusEncerrado) {
                continue;
            }

            List<AgendamentoItem> itens = agendamentoItemRepository.findByAgendamentoId(agendamento.getId());
            for (AgendamentoItem item : itens) {
                LocalDateTime inicioExistente = item.getInicioAtendimento();
                LocalDateTime fimExistente = item.getFimAtendimento();

                if (inicioExistente == null || fimExistente == null) {
                    continue;
                }

                boolean conflita = novoInicio.isBefore(fimExistente)
                        && novoFim.isAfter(inicioExistente);

                if (conflita) {
                    throw new EntityConflictException(
                            "O profissional já possui um agendamento nesse horário"
                    );
                }
            }
        }
    }
}
