package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.profissional.ClientePorProfissionalDto;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.repository.ClienteAgregado;

import java.time.LocalDate;
import java.util.List;

public class ProfissionalMapper {
    public static ClientePorProfissionalDto toResponse(
            Cliente cliente,
            String nomeFuncionario,
            LocalDate ultimaVisita,
            Double totalGasto,
            String servicoPreferido
    ) {
        ClientePorProfissionalDto response = new ClientePorProfissionalDto();

        response.setId(cliente.getId());
        response.setClienteId(cliente.getId());
        response.setNomeCliente(cliente.getNome());
        response.setTelefone(cliente.getTelefone());
        response.setNoShow(cliente.getTotalNoShows());
        response.setNomeFuncionario(nomeFuncionario);
        response.setEmail(cliente.getUsuario().getEmail());
        response.setUltimaVisita(ultimaVisita);
        response.setTotalGasto(totalGasto);
        response.setServicoPreferido(servicoPreferido);

        return response;
    }
}
