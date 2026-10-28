package many.studio.web_backend.controller;

import many.studio.web_backend.dto.agendamento.AgendamentoAnaliseResponse;
import many.studio.web_backend.dto.agendamento.ProbabilidadeNoShowRequest;
import many.studio.web_backend.dto.agendamento.ProbabilidadeNoShowResponse;
import many.studio.web_backend.dto.usuario.UsuarioDetalhesDto;
import many.studio.web_backend.service.AnaliseAgendamentosService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/analise")
public class AnaliseAgendamentosController {
    private final AnaliseAgendamentosService service;

    public AnaliseAgendamentosController(
            AnaliseAgendamentosService service) {
        this.service = service;
    }

    @GetMapping("/agendamentos")
    public ResponseEntity<List<AgendamentoAnaliseResponse>> buscarPorData(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data) {

        return ResponseEntity.ok(
                service.buscarPorData(data)
        );
    }

    @PostMapping("/probabilidade_no_show")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ProbabilidadeNoShowResponse> buscarProbabilidadeNoShow(
            @Valid @RequestBody ProbabilidadeNoShowRequest request,
            Authentication authentication) {

        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();
        String role = usuario.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(service.buscarProbabilidadeNoShow(
                request.getIdAgendamento(),
                request.getDataAgendamento(),
                role
        ));
    }
}
