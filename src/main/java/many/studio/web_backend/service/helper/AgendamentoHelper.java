package many.studio.web_backend.service.helper;

import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public AgendamentoHelper(AgendamentoRepository agendamentoRepository, AgendamentoItemRepository agendamentoItemRepository, ClienteRepository clienteRepository, StatusAgendamentoRepository statusAgendamentoRepository, ServicoRepository servicoRepository, ProfissionalRepository profissionalRepository, UsuarioRepository usuarioRepository, PacoteRepository pacoteRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoItemRepository = agendamentoItemRepository;
        this.clienteRepository = clienteRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
        this.usuarioRepository = usuarioRepository;
        this.pacoteRepository = pacoteRepository;
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

    public void validarConflitoHorarioAgendamento(LocalDateTime horario, Long idProfissional, Long pacoteId
    ) {
        List<Agendamento> agendamentos = agendamentoRepository.findByProfissionalId(idProfissional);

        Pacote pacote =  pacoteRepository.findById(pacoteId).get();

        Servico servico = pacote.getServico();

        LocalDateTime novoInicio = horario;
        LocalDateTime novoFim = horario.plusMinutes(servico.getDuracaoMinutos());

        for (Agendamento a : agendamentos) {

            List<AgendamentoItem> itens = agendamentoItemRepository.findByAgendamentoId(a.getId());
            AgendamentoItem item = itens.getFirst();

            LocalDateTime inicioExistente = item.getInicioAtendimento();
            LocalDateTime fimExistente = item.getFimAtendimento();

            boolean conflita = novoInicio.isBefore(fimExistente) && novoFim.isAfter(inicioExistente);

            if (conflita && !a.getStatusAgendamento().equals("cancelado")) {
                throw new EntityConflictException("O profissional já possui um agendamento nesse horário");
            }
        }
    }
}
