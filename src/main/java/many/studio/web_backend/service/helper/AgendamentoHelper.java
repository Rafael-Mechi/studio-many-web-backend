package many.studio.web_backend.service.helper;

import many.studio.web_backend.repository.*;

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


}
