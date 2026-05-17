package many.studio.web_backend.strategy.usuario;

import many.studio.web_backend.dto.UsuarioCriacaoDto;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.repository.ClienteRepository;
import many.studio.web_backend.strategy.UsuarioCriacaoStrategy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClienteCriacaoStrategy implements UsuarioCriacaoStrategy {

    private final ClienteRepository clienteRepository;

    public ClienteCriacaoStrategy(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public boolean suporta(Long perfilId) {
        return perfilId == 3L;
    }

    @Override
    public void criarDadosComplementares(Usuario usuario, UsuarioCriacaoDto dto) {

        Cliente cliente = new Cliente();

        cliente.setNome(dto.getNome());
        cliente.setTelefone(dto.getTelefone());
        cliente.setDocumento(dto.getDocumento());
        cliente.setTotalNoShows(0);
        cliente.setBloqueadoMotivo(null);
        cliente.setLgpdConsentimento(true);
        cliente.setUsuario(usuario);

        clienteRepository.save(cliente);
    }
}