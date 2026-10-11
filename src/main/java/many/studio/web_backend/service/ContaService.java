package many.studio.web_backend.service;

import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.StatusAgendamento;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.AgendamentoRepository;
import many.studio.web_backend.repository.ClienteRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import many.studio.web_backend.repository.StatusAgendamentoRepository;
import many.studio.web_backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContaService {

    private static final String MOTIVO_DESATIVACAO = "Conta desativada pelo usuário";

    private final UsuarioRepository usuarioRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ClienteRepository clienteRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final StatusAgendamentoRepository statusAgendamentoRepository;

    public ContaService(UsuarioRepository usuarioRepository,
                        ProfissionalRepository profissionalRepository,
                        ClienteRepository clienteRepository,
                        AgendamentoRepository agendamentoRepository,
                        StatusAgendamentoRepository statusAgendamentoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.profissionalRepository = profissionalRepository;
        this.clienteRepository = clienteRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
    }

    @Transactional
    public void desativarMinhaConta(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            return;
        }
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);

        StatusAgendamento cancelado = statusAgendamentoRepository
                .findByEstado("cancelado")
                .orElseThrow(() -> new EntityNotFoundException("Status cancelado não encontrado"));

        LocalDateTime agora = LocalDateTime.now();

        profissionalRepository.findByUsuario_Id(usuarioId).ifPresent(profissional ->
                cancelarFuturos(agendamentoRepository.findFuturosAtivosByProfissionalId(profissional.getId()),
                        cancelado, agora));

        clienteRepository.findByUsuario_Id(usuarioId).ifPresent(cliente ->
                cancelarFuturos(futurosDoCliente(usuarioId, agora), cancelado, agora));
    }

    private List<Agendamento> futurosDoCliente(Long usuarioId, LocalDateTime agora) {
        return agendamentoRepository.findByClienteUsuarioId(usuarioId).stream()
                .filter(a -> !isTerminal(a))
                .filter(a -> a.getItens() != null && a.getItens().stream()
                        .anyMatch(item -> item.getInicioAtendimento() != null
                                && item.getInicioAtendimento().isAfter(agora)))
                .toList();
    }

    private boolean isTerminal(Agendamento agendamento) {
        if (agendamento.getStatusAgendamento() == null
                || agendamento.getStatusAgendamento().getEstado() == null) {
            return false;
        }
        String estado = agendamento.getStatusAgendamento().getEstado();
        return estado.equalsIgnoreCase("cancelado")
                || estado.equalsIgnoreCase("concluido")
                || estado.equalsIgnoreCase("recusado")
                || estado.equalsIgnoreCase("faltou");
    }

    private void cancelarFuturos(List<Agendamento> agendamentos, StatusAgendamento cancelado, LocalDateTime agora) {
        for (Agendamento agendamento : agendamentos) {
            agendamento.setStatusAgendamento(cancelado);
            agendamento.setCanceladoEm(agora);
            agendamento.setCancelamentoMotivo(MOTIVO_DESATIVACAO);
        }
        agendamentoRepository.saveAll(agendamentos);
    }
}
