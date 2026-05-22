package many.studio.web_backend.service;

import many.studio.web_backend.dto.profissional.ClientePorProfissionalDto;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.ProfissionalMapper;
import many.studio.web_backend.repository.AgendamentoRepository;
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

    public List<ClientePorProfissionalDto> listarClientesPorFuncionarioId(Long funcionarioId){
        String nomeDoProfissional = profissionalRepository.findById(funcionarioId)
                .map(Profissional::getNome)
                .orElse("Profissional não encontrado");

        List<Cliente> clientes = clienteRepository.findClientesByProfissionalId(funcionarioId);

        List<ClientePorProfissionalDto> responseList = new ArrayList<>();

        for (Cliente cliente : clientes) {
            LocalDate ultimaVisita = agendamentoRepository.findUltimaVisitaByClienteId(cliente.getId());
            LocalDate dataVisita = (ultimaVisita != null) ? ultimaVisita : LocalDate.now();

            Double totalGasto = agendamentoRepository.findTotalGastoByClienteId(cliente.getId());
            Double valorGasto = (totalGasto != null) ? totalGasto : 0.0;

            List<String> servicos = agendamentoRepository.findServicoPreferidoByClienteId(cliente.getId());
            String preferido = !servicos.isEmpty() ? servicos.get(0) : "Nenhum serviço";

            ClientePorProfissionalDto dto = ProfissionalMapper.toResponse(
                    cliente, nomeDoProfissional, dataVisita, valorGasto, preferido
            );

            responseList.add(dto);
        }
        return responseList;
    }
}
