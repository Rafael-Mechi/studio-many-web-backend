package many.studio.web_backend.service;

import many.studio.web_backend.dto.profissional.ClientePorProfissionalDto;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.ProfissionalMapper;
import many.studio.web_backend.repository.AgendamentoRepository;
import many.studio.web_backend.repository.ClienteAgregado;
import many.studio.web_backend.repository.ClienteRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
