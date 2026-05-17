package many.studio.web_backend.service;

import jakarta.persistence.EntityNotFoundException;
import many.studio.web_backend.config.GerenciadorTokenJwt;
import many.studio.web_backend.dto.usuario.*;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Perfil;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.mapper.UsuarioMapper;
import many.studio.web_backend.repository.ClienteRepository;
import many.studio.web_backend.repository.PerfilRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import many.studio.web_backend.repository.UsuarioRepository;
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
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private List<UsuarioCriacaoStrategy> strategies;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;


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

        Integer perfilId = usuario.getPerfil().getId();

        if (perfilId == 3) {
            Cliente cliente = clienteRepository.findByUsuario_Id(usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
            if (dto.getNome() != null) cliente.setNome(dto.getNome());
            if (dto.getTelefone() != null) cliente.setTelefone(dto.getTelefone());
            if (dto.getDocumento() != null) cliente.setDocumento(dto.getDocumento());
            clienteRepository.save(cliente);
        } else if (perfilId == 2) {
            Profissional profissional = profissionalRepository.findByUsuario_Id(usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));
            if (dto.getNome() != null) profissional.setNome(dto.getNome());
            profissionalRepository.save(profissional);
        }
    }

    private Usuario obterUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autenticado");
        }
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }
}
