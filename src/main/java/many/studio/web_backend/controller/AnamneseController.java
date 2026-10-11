package many.studio.web_backend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import many.studio.web_backend.dto.anamnese.AnamneseListResponse;
import many.studio.web_backend.dto.anamnese.AnamneseRequest;
import many.studio.web_backend.dto.anamnese.AnamneseResponse;
import many.studio.web_backend.dto.usuario.UsuarioDetalhesDto;
import many.studio.web_backend.exception.ForbiddenException;
import many.studio.web_backend.service.AnamneseService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fichas")
public class AnamneseController {
    private final AnamneseService anamneseService;

    public AnamneseController(AnamneseService anamneseService) {
        this.anamneseService = anamneseService;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<AnamneseResponse> criarFicha(@RequestBody AnamneseRequest anamneseRequest, Authentication authentication){
        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();

        Long id = usuario.getId();
        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if(!role.equals("ROLE_ADMIN") && !role.equals("ROLE_PROFISSIONAL")){
            throw new ForbiddenException("Não foi possível realizar esta ação");
        }

        AnamneseResponse response = anamneseService.criarFicha(id, anamneseRequest);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Page<AnamneseListResponse>> listarFichas(
            Authentication authentication,
            @RequestParam(required = false) String busca,
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        UsuarioDetalhesDto usuario =
                (UsuarioDetalhesDto) authentication.getPrincipal();

        Long id = usuario.getId();

        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (!role.equals("ROLE_ADMIN") && !role.equals("ROLE_PROFISSIONAL")) {
            throw new ForbiddenException("Não foi possível realizar esta ação");
        }

        Page<AnamneseListResponse> response =
                anamneseService.listarFichas(id, role, busca, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<AnamneseResponse> buscarFichaPorId(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();

        Long usuarioId = usuario.getId();
        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (!role.equals("ROLE_ADMIN") && !role.equals("ROLE_PROFISSIONAL")) {
            throw new ForbiddenException("Não foi possível realizar esta ação");
        }

        AnamneseResponse response = anamneseService.buscarFichaPorId(id, usuarioId, role);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<AnamneseResponse> atualizarFicha(
            @PathVariable Long id,
            @RequestBody AnamneseRequest anamneseRequest,
            Authentication authentication
    ) {
        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();

        Long usuarioId = usuario.getId();
        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (!role.equals("ROLE_ADMIN") && !role.equals("ROLE_PROFISSIONAL")) {
            throw new ForbiddenException("Não foi possível realizar esta ação");
        }

        AnamneseResponse response = anamneseService.atualizarFicha(id, anamneseRequest, usuarioId, role);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> excluirFicha(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();

        Long usuarioId = usuario.getId();
        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (!role.equals("ROLE_ADMIN") && !role.equals("ROLE_PROFISSIONAL")) {
            throw new ForbiddenException("Não foi possível realizar esta ação");
        }

        anamneseService.excluirFicha(id, usuarioId, role);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{clienteId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<AnamneseResponse> buscarFichaPorCliente(
            @PathVariable Long clienteId,
            Authentication authentication
    ) {
        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();

        Long usuarioId = usuario.getId();
        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (!role.equals("ROLE_ADMIN") && !role.equals("ROLE_PROFISSIONAL")) {
            throw new ForbiddenException("Não foi possível realizar esta ação");
        }

        AnamneseResponse response = anamneseService.buscarFichaPorCliente(clienteId, usuarioId, role);

        return ResponseEntity.ok(response);
    }

}
