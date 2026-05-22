package many.studio.web_backend.service;

import jakarta.transaction.Transactional;
import many.studio.web_backend.dto.profissional.*;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.ProfissionalMapper;
import many.studio.web_backend.repository.AgendamentoRepository;
import many.studio.web_backend.repository.ClienteAgregado;
import many.studio.web_backend.repository.ClienteRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProfissionalService {
    private final ClienteRepository clienteRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ProfissionalRepository profissionalRepository;

    public ProfissionalService(ClienteRepository clienteRepository, AgendamentoRepository agendamentoRepository, ProfissionalRepository profissionalRepository) {
        this.clienteRepository = clienteRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.profissionalRepository = profissionalRepository;
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
            LocalDate ultimaVisita = item.getUltimaVisita();
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
        LocalDate ultimaVisita = agregado.getUltimaVisita();
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

        profissional.setNome(dto.nome());

        Usuario usuario = profissional.getUsuario();
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());

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
}
