package many.studio.web_backend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import many.studio.web_backend.dto.profissional.*;
import many.studio.web_backend.dto.usuario.UsuarioDetalhesDto;
import many.studio.web_backend.service.ProfissionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {
    private final ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @GetMapping("/{profissionalId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ProfissionalDetalhesDto> getDetalhesProfissional(@PathVariable Long profissionalId, Authentication authentication) {
        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();

        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        return ResponseEntity.ok().body(profissionalService.getDetalhesProfissionais(profissionalId, role));
    }

    @PutMapping("/{profissionalId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ProfissionalResponseDto> atualizarProfissional(
            @PathVariable Long profissionalId,
            @Valid @RequestBody ProfissionalUpdateDto dto
    ){
        ProfissionalResponseDto response = profissionalService.atualizarProfissional(profissionalId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{profissionalId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletarProfissional(@PathVariable Long profissionalId){
        profissionalService.deletarProfissional(profissionalId);
        return ResponseEntity.noContent().build();
    }
}
