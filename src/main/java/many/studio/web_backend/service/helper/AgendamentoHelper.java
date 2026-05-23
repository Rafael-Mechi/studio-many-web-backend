package many.studio.web_backend.service.helper;

import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.repository.*;
import org.springframework.stereotype.Component;

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

    public AgendamentoHelper(AgendamentoRepository agendamentoRepository, AgendamentoItemRepository agendamentoItemRepository, ClienteRepository clienteRepository, StatusAgendamentoRepository statusAgendamentoRepository, ServicoRepository servicoRepository, ProfissionalRepository profissionalRepository, UsuarioRepository usuarioRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoItemRepository = agendamentoItemRepository;
        this.clienteRepository = clienteRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
        this.usuarioRepository = usuarioRepository;
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
}
