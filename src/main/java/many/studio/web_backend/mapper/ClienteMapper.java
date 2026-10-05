package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.ClienteBuscaResponse;
import many.studio.web_backend.entity.Cliente;

public class ClienteMapper {
    public static ClienteBuscaResponse toBuscaResponse(Cliente cliente) {
        return new ClienteBuscaResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTelefone()
        );
    }
}
