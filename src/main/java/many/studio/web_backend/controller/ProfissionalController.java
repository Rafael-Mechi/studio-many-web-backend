package many.studio.web_backend.controller;

import jakarta.websocket.server.PathParam;
import many.studio.web_backend.dto.profissional.ClientePorProfissionalDto;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.service.ProfissionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {
    private final ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @GetMapping("/{funcionarioId}/clientes")
    public ResponseEntity<List<ClientePorProfissionalDto>> listarClientesPorProfissional(
            @PathVariable Long funcionarioId
    ){
        List<ClientePorProfissionalDto> response = profissionalService.listarClientesPorFuncionarioId(funcionarioId);
        return ResponseEntity.ok(response);
    }
}
