package many.studio.web_backend.service;

import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.config.GerenciadorTokenJwt;
import many.studio.web_backend.dto.config.MeuPerfilUpdateRequest;
import many.studio.web_backend.dto.usuario.*;
import many.studio.web_backend.mapper.config.MeuPerfilMapper;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.exception.ForbiddenException;
import many.studio.web_backend.mapper.UsuarioMapper;
import many.studio.web_backend.repository.*;
import many.studio.web_backend.strategy.UsuarioCriacaoStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;
    private final List<UsuarioCriacaoStrategy> strategies;
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ServicoRepository servicoRepository;
    private final AnamneseClienteRepository anamneseClienteRepository;

    public UsuarioService(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, GerenciadorTokenJwt gerenciadorTokenJwt, AuthenticationManager authenticationManager, List<UsuarioCriacaoStrategy> strategies, ClienteRepository clienteRepository, ProfissionalRepository profissionalRepository, AgendamentoRepository agendamentoRepository, ServicoRepository servicoRepository, AnamneseClienteRepository anamneseClienteRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
        this.strategies = strategies;
        this.clienteRepository = clienteRepository;
        this.profissionalRepository = profissionalRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.servicoRepository = servicoRepository;
        this.anamneseClienteRepository = anamneseClienteRepository;
    }

    public void criar(UsuarioCriacaoDto dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EntityConflictException("Usuário com email existente");
        }

        Perfil perfil = perfilRepository.findById(dto.getPerfilId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Perfil não encontrado"));

        Usuario novoUsuario = UsuarioMapper.of(dto);

        novoUsuario.setPerfil(perfil);
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        novoUsuario.setAtivo(true);
        novoUsuario.setCriadoEm(LocalDateTime.now());

        usuarioRepository.save(novoUsuario);

        UsuarioCriacaoStrategy strategy = strategies.stream()
                .filter(s -> s.suporta(dto.getPerfilId()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Perfil inválido"));

        strategy.criarDadosComplementares(novoUsuario, dto);
    }

    public UsuarioTokenDto autenticar(Usuario usuario) {

        final UsernamePasswordAuthenticationToken credentials =
                new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuario usuarioAutenticado = usuarioRepository.findByEmail(usuario.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos"));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        // Retorna id + email + token — sem nome, pois não existe em 'usuarios'
        return UsuarioMapper.of(usuarioAutenticado, token);
    }

    public List<UsuarioListarDto> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::of)
                .toList();
    }

    public void atualizar(Long id, UsuarioAtualizarDto dto) {

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (dto.getEmail() != null && !dto.getEmail().equals(usuarioExistente.getEmail())) {
            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new EntityConflictException("Email já cadastrado");
            }
            usuarioExistente.setEmail(dto.getEmail());
        }

        if (dto.getSenha() != null) {
            usuarioExistente.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        usuarioRepository.save(usuarioExistente);
    }

    public void removerPorId(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public void atualizarSenha(UsuarioAtualizarSenhaDto dto) {
        Usuario usuario = obterUsuarioAutenticado();

        if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha atual inválida");
        }

        usuario.setSenha(passwordEncoder.encode(dto.getSenhaNova()));
        usuarioRepository.save(usuario);
    }

    public void redefinirSenha(UsuarioRedefinirSenhaDto dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        usuario.setSenha(passwordEncoder.encode(dto.getSenhaNova()));
        usuarioRepository.save(usuario);
    }

    public void atualizarMeuPerfil(Long usuarioId, MeuPerfilUpdateRequest dto) {
        String nome = dto.getNome() != null ? dto.getNome().trim() : "";
        if (nome.length() < 3 || nome.length() > 75) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome deve ter entre 3 e 75 caracteres");
        }
        String telefone = dto.getTelefone() != null ? dto.getTelefone().replaceAll("\\D", "") : "";
        if (telefone.length() < 10 || telefone.length() > 11) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone inválido");
        }

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Optional<Profissional> profissionalOpt = profissionalRepository.findByUsuario_Id(usuarioId);
        if (profissionalOpt.isPresent()) {
            Profissional profissional = profissionalOpt.get();
            profissional.setNome(nome);
            profissional.setTelefone(telefone);
            profissionalRepository.save(profissional);
            return;
        }

        Cliente cliente = clienteRepository.findByUsuario_Id(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Dados de perfil não encontrados"));
        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        clienteRepository.save(cliente);
    }

    public void atualizarPerfil(UsuarioAtualizarPerfilDto dto) {
        if (dto.getEmail() == null && dto.getNome() == null
                && dto.getTelefone() == null && dto.getDocumento() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhum campo para atualizar");
        }

        Usuario usuario = obterUsuarioAutenticado();

        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new EntityConflictException("Email já cadastrado");
            }
            usuario.setEmail(dto.getEmail());
            usuarioRepository.save(usuario);
        }

        Long perfilId = usuario.getPerfil().getId();

        if (perfilId.equals(3L)) {
            Cliente cliente = clienteRepository.findByUsuario_Id(usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
            if (dto.getNome() != null) cliente.setNome(dto.getNome());
            if (dto.getTelefone() != null) cliente.setTelefone(dto.getTelefone());
            if (dto.getDocumento() != null) cliente.setDocumento(dto.getDocumento());
            clienteRepository.save(cliente);
        } else if (perfilId.equals(2L)) {
            Profissional profissional = profissionalRepository.findByUsuario_Id(usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));
            if (dto.getNome() != null) profissional.setNome(dto.getNome());
            profissionalRepository.save(profissional);
        }
    }

    private Usuario obterUsuarioAutenticado() {        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autenticado");
        }
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public Usuario findById(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        Usuario usuario = usuarioOpt.get();

        if(usuario == null){
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        return usuario;
    }

    /**
     * Perfil do usuário logado (tela de configurações). Retorna
     * {@link MeuPerfilProfissionalResponse} para ADMIN/PROFISSIONAL e
     * {@link MeuPerfilClienteResponse} para CLIENTE. Os campos
     * {@code role}, {@code clienteId} e {@code clienteTelefone} são mantidos
     * porque o front os usa na sessão (navegação e fluxo de agendamento).
     */
    public Object buscarMeuPerfil(UsuarioDetalhesDto usuarioDetalhesDto){
        String role = usuarioDetalhesDto.getAuthorities()
                .stream()
                .findFirst()
                .map(authority -> authority.getAuthority())
                .orElse(null);

        Usuario usuario = usuarioRepository.findById(usuarioDetalhesDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (role != null && (role.equalsIgnoreCase("ROLE_ADMIN") || role.equalsIgnoreCase("ROLE_PROFISSIONAL"))) {
            Profissional profissional = profissionalRepository.findByUsuario_Id(usuarioDetalhesDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

            return MeuPerfilMapper.toProfissionalResponse(
                    usuario,
                    profissional,
                    role,
                    agendamentoRepository.findByProfissionalId(profissional.getId()).size(),
                    clienteRepository.findClientesByProfissionalUsuarioId(usuarioDetalhesDto.getId()).size());
        }

        Cliente cliente = clienteRepository.findByUsuario_Id(usuarioDetalhesDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        return MeuPerfilMapper.toClienteResponse(
                usuario,
                cliente,
                role,
                agendamentoRepository.findByClienteUsuarioId(usuarioDetalhesDto.getId()).size(),
                (int) anamneseClienteRepository.countByClienteId(cliente.getId()));
    }

    public List<ClienteResponse> getClientes(Long id, String role) {
        List<ClienteResponse> clienteResponses = new ArrayList<>();

        List<Cliente> clientesAlvo;
        Long usuarioIdFiltro;

        if (role.equals("ROLE_CLIENTE")) {
            throw new ForbiddenException("Você não tem permissão para acessar esse recurso");

        } else if (role.equals("ROLE_ADMIN")) {
            clientesAlvo = clienteRepository.findAll();
            usuarioIdFiltro = null;

        } else if (role.equals("ROLE_PROFISSIONAL")) {
            clientesAlvo = clienteRepository.findClientesByProfissionalUsuarioId(id);
            usuarioIdFiltro = id;

        } else {
            throw new IllegalArgumentException("Role inválida: " + role);
        }

        for (Cliente c : clientesAlvo) {
            ClienteResponse clienteResponse = new ClienteResponse();
            Optional<Usuario> u = clienteRepository.findUsuarioByClienteId(c.getId());

            clienteResponse.setId(c.getId());
            clienteResponse.setNome(c.getNome());
            clienteResponse.setEmail(u.get().getEmail());
            clienteResponse.setClienteDesde(u.get().getCriadoEm().toLocalDate());
            clienteResponse.setCpf(c.getDocumento());
            clienteResponse.setTelefone(c.getTelefone());
            clienteResponse.setQtdNoShows(c.getTotalNoShows());

            List<HistoricoAgendamentos> historicoAgendamentos =
                    agendamentoRepository.buscarHistoricoPorClienteEUsuarioProfissional(c.getId(), usuarioIdFiltro);

            clienteResponse.setHistoricoAgendamentos(historicoAgendamentos);

            Double totalGasto = historicoAgendamentos.stream()
                    .filter(h -> "concluido".equalsIgnoreCase(h.getStatus()))
                    .map(HistoricoAgendamentos::getValor)
                    .filter(Objects::nonNull)
                    .mapToDouble(Double::doubleValue)
                    .sum();

            clienteResponse.setTotalGasto(totalGasto);

            Optional<HistoricoAgendamentos> ultimoAtendimentoRealizado = historicoAgendamentos.stream()
                    .filter(h -> "concluido".equalsIgnoreCase(h.getStatus())
                            || "em atendimento".equalsIgnoreCase(h.getStatus()))
                    .findFirst();

            clienteResponse.setUltimaVisita(
                    ultimoAtendimentoRealizado.map(h -> h.getDataHora().toLocalDate()).orElse(null)
            );

            clienteResponses.add(clienteResponse);
        }

        return clienteResponses;
    }

    public List<ProfissionalResponse> getProfissionais(Long id, String role) {
        List<ProfissionalResponse> profissionalResponses = new ArrayList<>();
        List<Profissional> profissionais;

        if (role.equals("ROLE_CLIENTE") || role.equals("ROLE_PROFISSIONAL")) {
            throw new ForbiddenException("Você não tem permissão para acessar esse recurso");
        } else if (role.equals("ROLE_ADMIN")){
            profissionais = profissionalRepository.findAll();
        } else {
            throw new IllegalArgumentException("Role inválida: " + role);
        }

        for (Profissional p : profissionais){
            ProfissionalResponse profissionalResponse = new ProfissionalResponse();

            profissionalResponse.setId(p.getId());
            profissionalResponse.setNome(p.getNome());
            profissionalResponse.setTelefone(p.getTelefone());
            profissionalResponse.setEmail(p.getUsuario().getEmail());

            List<String> categorias = agendamentoRepository.findCategoriasByProfissionalId(p.getId());
            String especialidade = categorias.isEmpty() ? null : String.join(", ", categorias);
            profissionalResponse.setEspecialidade(especialidade);

            Long qtdAgendamentos = agendamentoRepository.countAgendamentosConcluidosPorProfissional(p.getId());
            profissionalResponse.setQtdAgendamentos(qtdAgendamentos != null ? qtdAgendamentos.intValue() : 0);

            profissionalResponses.add(profissionalResponse);
        }

        return profissionalResponses;
    }
}
