package many.studio.web_backend.service;

import jakarta.transaction.Transactional;
import many.studio.web_backend.dto.profissional.*;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.exception.ForbiddenException;
import many.studio.web_backend.mapper.ProfissionalMapper;
import many.studio.web_backend.mapper.profissional.ProfissionalDetalhesMapper;
import many.studio.web_backend.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfissionalService {
    private final ClienteRepository clienteRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final ServicoProfissionalRepository servicoProfissionalRepository;
    private final ServicoRepository servicoRepository;
    private final DiasDeTrabalhoRepository diasDeTrabalhoRepository;

    public ProfissionalService(ClienteRepository clienteRepository, AgendamentoRepository agendamentoRepository, ProfissionalRepository profissionalRepository, PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository, ServicoProfissionalRepository servicoProfissionalRepository, ServicoRepository servicoRepository, DiasDeTrabalhoRepository diasDeTrabalhoRepository) {
        this.clienteRepository = clienteRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.profissionalRepository = profissionalRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.servicoProfissionalRepository = servicoProfissionalRepository;
        this.servicoRepository = servicoRepository;
        this.diasDeTrabalhoRepository = diasDeTrabalhoRepository;
    }

    public List<ClientePorProfissionalDto> listarClientesPorProfissionalId(Long profissionalId){
        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não existe"));

        List<ClienteAgregado> agregados = clienteRepository.findClientesByProfissionalId(profissionalId);
        if (agregados.isEmpty()){
            throw new EntityNotFoundException("Nenhum cliente para esse profissional");
        }

        return agregados.stream().map(item -> {
            Cliente cliente = item.getCliente();
            LocalDateTime ultimaVisita = item.getUltimaVisita();
            Double totalGasto = (item.getTotalGasto() != null) ? item.getTotalGasto() : 0.0;
            List<String> servicos = agendamentoRepository.findServicoPreferidoByClienteId(cliente.getId());
            String preferido = (!servicos.isEmpty()) ? servicos.get(0) : "Nenhum serviço";

            return ProfissionalMapper.toResponse(
                    cliente,
                    profissional.getNome(),
                    ultimaVisita,
                    totalGasto,
                    preferido);
        }).toList();
    }

    public ClienteDetalheDto detalharClientePorProfissional(Long profissionalId, Long clienteId) {
        if (!profissionalRepository.existsById(profissionalId)) {
            throw new EntityNotFoundException("Profissional não existe");
        }

        ClienteAgregado agregado = clienteRepository.findClienteByProfissionalIdEClienteId(profissionalId, clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado para este profissional"));

        Cliente cliente = agregado.getCliente();
        LocalDateTime ultimaVisita = agregado.getUltimaVisita();
        Double totalGasto = (agregado.getTotalGasto() != null) ? agregado.getTotalGasto() : 0.0;

        List<AgendamentoHistoricoDto> historico = agendamentoRepository.findHistoricoRecenteByClienteId(clienteId);

        return new ClienteDetalheDto(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTelefone(),
                cliente.getDocumento(),
                cliente.getUsuario().getEmail(),
                cliente.getTotalNoShows(),
                ultimaVisita,
                totalGasto,
                historico
        );
    }

    @Transactional
    public ProfissionalResponseDto atualizarProfissional(Long profissionalId, ProfissionalUpdateDto dto) {
        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        profissional.setNome(dto.getNome());

        Usuario usuario = profissional.getUsuario();

        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new EntityConflictException("Email já cadastrado");
            }
            usuario.setEmail(dto.getEmail());
        }

        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        List<ServicoProfissional> servicosRealizados = servicoProfissionalRepository.findAllByProfissionalId(profissionalId);
        List<String> nomeServicos = new ArrayList<>();

        for(ServicoProfissional servico : servicosRealizados) {
            nomeServicos.add(servico.getServico().getNome());
        }

        for(ServicoProfissional servico : servicosRealizados) {
            if(!dto.getServicos().contains(servico.getServico().getNome())) {
                servicoProfissionalRepository.delete(servico);
            }
        }

        for(String servico : dto.getServicos()) {
            if(!nomeServicos.contains(servico)) {
                if(!servicoRepository.existsByNome(servico)) {
                    throw new EntityNotFoundException("Serviço não encontrado");
                }

                Optional<Servico> servicoEncontrado = servicoRepository.findByNome(servico);
                ServicoProfissional servicoProfissional = new ServicoProfissional();
                servicoProfissional.setProfissional(profissional);
                servicoProfissional.setServico(servicoEncontrado.get());

                servicoProfissionalRepository.save(servicoProfissional);
            }
        }

        List<DiasDeTrabalho> diasDeTrabalho = diasDeTrabalhoRepository.findByProfissionalId(profissionalId);
        List<ProfissionalUpdateDto.DiasDeTrabalhoDto> diasEnviados = dto.getDiasDeTrabalho();

        List<String> diasSemanaEnviados = new ArrayList<>();
        List<String> diasSemanaCadastrados = new ArrayList<>();

        for(DiasDeTrabalho dias : diasDeTrabalho) {
            diasSemanaCadastrados.add(dias.getDiaDaSemana().toString());
        }

        for(ProfissionalUpdateDto.DiasDeTrabalhoDto dias : diasEnviados) {
            diasSemanaEnviados.add(dias.getDiaSemana());
        }

        for(ProfissionalUpdateDto.DiasDeTrabalhoDto diaEnviado : dto.getDiasDeTrabalho()) {
            if(!diasSemanaCadastrados.contains(diaEnviado.getDiaSemana())) {

                DiasDeTrabalho diaNovo = new DiasDeTrabalho();
                diaNovo.setHoraInicio(diaEnviado.getInicio());
                diaNovo.setHoraFim(diaEnviado.getFim());
                diaNovo.setProfissional(profissional);
                diaNovo.setDiaDaSemana(DayOfWeek.valueOf(diaEnviado.getDiaSemana()));

                diasDeTrabalhoRepository.save(diaNovo);
            }
        }

        profissionalRepository.save(profissional);

        return new ProfissionalResponseDto(
                profissional.getId(),
                profissional.getNome(),
                usuario.getEmail()
        );
    }

    @Transactional
    public void deletarProfissional(Long profissionalId) {
        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        Usuario usuario = profissional.getUsuario();
        usuario.setAtivo(false);

        profissionalRepository.save(profissional);
    }

    public Profissional findByUsuarioId(Long id) {
        Optional<Profissional> profissionalOpt = profissionalRepository.findByUsuario_Id(id);

        if(profissionalOpt.isEmpty()){
            throw new EntityNotFoundException("Profissional não encontrado");
        }

        return profissionalOpt.get();

    }

    public ProfissionalDetalhesDto getDetalhesProfissionais(Long id, String role) {
        if(!role.equals("ROLE_ADMIN")) {
            throw new ForbiddenException("Você não tem permissão para acessar esse recurs");
        }

        if(!profissionalRepository.existsById(id)) {
            throw new EntityNotFoundException("Profissional não encontrado");
        }

        Profissional profissional = profissionalRepository.findById(id).get();

        List<ServicoProfissional> servicos = servicoProfissionalRepository.findAllByProfissionalId(id);
        List<String> servicosRealizados = new ArrayList<>();

        for(ServicoProfissional servico : servicos) {
            servicosRealizados.add(servico.getServico().getNome());
        }

        return ProfissionalDetalhesMapper.toResponse(profissional, servicosRealizados);
    }
}
