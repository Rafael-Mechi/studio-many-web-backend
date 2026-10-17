package many.studio.web_backend.service;

import many.studio.web_backend.dto.usuario.ProfissionalCadastroDto;
import many.studio.web_backend.dto.usuario.ProfissionalCadastroResponse;
import many.studio.web_backend.dto.config.IntervaloDto;
import many.studio.web_backend.entity.Perfil;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Servico;
import many.studio.web_backend.entity.ServicoProfissional;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.PerfilRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import many.studio.web_backend.repository.ServicoProfissionalRepository;
import many.studio.web_backend.repository.ServicoRepository;
import many.studio.web_backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Service
public class ProfissionalCadastroService {

    private static final Long PERFIL_PROFISSIONAL_ID = 2L;

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ServicoRepository servicoRepository;
    private final ServicoProfissionalRepository servicoProfissionalRepository;
    private final PasswordEncoder passwordEncoder;
    private final HorarioTrabalhoService horarioTrabalhoService;

    public ProfissionalCadastroService(UsuarioRepository usuarioRepository,
                                       PerfilRepository perfilRepository,
                                       ProfissionalRepository profissionalRepository,
                                       ServicoRepository servicoRepository,
                                       ServicoProfissionalRepository servicoProfissionalRepository,
                                       PasswordEncoder passwordEncoder,
                                       HorarioTrabalhoService horarioTrabalhoService) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.profissionalRepository = profissionalRepository;
        this.servicoRepository = servicoRepository;
        this.servicoProfissionalRepository = servicoProfissionalRepository;
        this.passwordEncoder = passwordEncoder;
        this.horarioTrabalhoService = horarioTrabalhoService;
    }

    @Transactional
    public ProfissionalCadastroResponse cadastrar(ProfissionalCadastroDto dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EntityConflictException("Usuário com email existente");
        }

        Perfil perfil = perfilRepository.findById(PERFIL_PROFISSIONAL_ID)
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));

        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        novoUsuario.setPerfil(perfil);
        novoUsuario.setAtivo(true);
        novoUsuario.setCriadoEm(LocalDateTime.now());
        usuarioRepository.save(novoUsuario);

        Profissional profissional = new Profissional();
        profissional.setNome(dto.getNome());
        profissional.setTelefone(dto.getTelefone());
        profissional.setDocumento(dto.getDocumento());
        if (dto.getFotoUrl() != null && !dto.getFotoUrl().trim().isEmpty()) {
            profissional.setFotoUrl(dto.getFotoUrl().trim());
        }
        profissional.setUsuario(novoUsuario);
        profissionalRepository.save(profissional);

        // Horários são opcionais: se informados, aplica a mesma lógica de Configurações
        // com almoço padrão 12h-13h quando não informado.
        if (dto.getHorarios() != null) {
            if (dto.getHorarios().getAlmoco() == null) {
                dto.getHorarios().setAlmoco(new IntervaloDto(LocalTime.of(12, 0), LocalTime.of(13, 0)));
            }
            horarioTrabalhoService.salvar(novoUsuario.getId(), dto.getHorarios());
        }

        // Serviços marcados no cadastro são vinculados mesmo sem jornada,
        // para não perder a informação de "o que ele realiza".
        if (dto.getServicosIds() != null && !dto.getServicosIds().isEmpty()) {
            vincularServicos(profissional, dto.getServicosIds());
        }

        return new ProfissionalCadastroResponse(novoUsuario.getId(), profissional.getId());
    }

    private void vincularServicos(Profissional profissional, List<Long> servicosIds) {
        Map<Long, Servico> servicos = new HashMap<>();
        servicoRepository.findAllById(servicosIds)
                .forEach(servico -> servicos.put(servico.getId(), servico));

        for (Long servicoId : new LinkedHashSet<>(servicosIds)) {
            if (!servicos.containsKey(servicoId)) {
                throw new EntityNotFoundException("Serviço não encontrado");
            }
            if (servicoProfissionalRepository
                    .existsByProfissionalIdAndServicoId(profissional.getId(), servicoId)) {
                continue;
            }
            servicoProfissionalRepository.save(
                    new ServicoProfissional(servicos.get(servicoId), profissional));
        }
    }
}
