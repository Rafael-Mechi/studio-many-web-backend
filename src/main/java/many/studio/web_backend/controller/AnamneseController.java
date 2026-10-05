package many.studio.web_backend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import many.studio.web_backend.dto.anamnese.AnamneseListResponse;
import many.studio.web_backend.dto.anamnese.AnamneseRequest;
import many.studio.web_backend.dto.anamnese.AnamneseResponse;
import many.studio.web_backend.dto.usuario.UsuarioDetalhesDto;
import many.studio.web_backend.exception.ForbiddenException;
import many.studio.web_backend.service.AnamneseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Page<AnamneseListResponse>> listarFichas(Authentication authentication, @PageableDefault(size = 10) Pageable pageable){
        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();

        Long id = usuario.getId();
        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if(!role.equals("ROLE_ADMIN") && !role.equals("ROLE_PROFISSIONAL")){
            throw new ForbiddenException("Não foi possível realizar esta ação");
        }

        Page<AnamneseListResponse> response = anamneseService.listarFichas(id, role, pageable);

        return ResponseEntity.status(200).body(response);
    }

}
