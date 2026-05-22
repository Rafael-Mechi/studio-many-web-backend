package many.studio.web_backend.controller;

import jakarta.validation.Valid;
import many.studio.web_backend.dto.profissional.ClienteDetalheDto;
import many.studio.web_backend.dto.profissional.ClientePorProfissionalDto;
import many.studio.web_backend.dto.profissional.ProfissionalResponseDto;
import many.studio.web_backend.dto.profissional.ProfissionalUpdateDto;
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

    @GetMapping("/{profissionalId}/clientes/{clienteId}")
    public ResponseEntity<ClienteDetalheDto> listarClientePorProfissional(
            @PathVariable Long profissionalId,
            @PathVariable Long clienteId
    ){
        ClienteDetalheDto response = profissionalService.detalharClientePorProfissional(profissionalId, clienteId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{profissionalId}")
    public ResponseEntity<ProfissionalResponseDto> atualizarProfissional(
            @PathVariable Long profissionalId,
            @Valid @RequestBody ProfissionalUpdateDto dto
    ){
        ProfissionalResponseDto response = profissionalService.atualizarProfissional(profissionalId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{profissionalId}")
    public ResponseEntity<Void> deletarProfissional(@PathVariable Long profissionalId){
        profissionalService.deletarProfissional(profissionalId);
        return ResponseEntity.noContent().build();
    }
}
