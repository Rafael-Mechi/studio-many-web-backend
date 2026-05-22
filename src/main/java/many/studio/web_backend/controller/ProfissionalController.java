package many.studio.web_backend.controller;

import many.studio.web_backend.dto.profissional.ClientePorProfissionalDto;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.service.ProfissionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {
    private final ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @GetMapping("/{profissionalId}/clientes")
    public ResponseEntity<List<ClientePorProfissionalDto>> listarClientesPorProfissional(
            @PathVariable Long profissionalId
    ){
        List<ClientePorProfissionalDto> response = profissionalService.listarClientesPorProfissionalId(profissionalId);
        return ResponseEntity.ok(response);
    }
}
