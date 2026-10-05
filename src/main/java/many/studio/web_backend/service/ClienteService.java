package many.studio.web_backend.service;

import many.studio.web_backend.dto.ClienteBuscaResponse;
import many.studio.web_backend.mapper.ClienteMapper;
import many.studio.web_backend.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteBuscaResponse> buscarClientes(String busca) {

        if (busca == null || busca.isBlank()) {
            return List.of();
        }

        return clienteRepository.buscarPorNomeOuTelefone(busca)
                .stream()
                .map(ClienteMapper::toBuscaResponse)
                .toList();
    }
}
