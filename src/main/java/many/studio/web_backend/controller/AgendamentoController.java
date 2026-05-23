package many.studio.web_backend.controller;

import jakarta.validation.Valid;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoRequest;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.dto.agendamento.CancelarAgendamentoRequest;
import many.studio.web_backend.dto.usuario.UsuarioDetalhesDto;
import many.studio.web_backend.mapper.AgendamentoMapper;
import many.studio.web_backend.service.AgendamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<AgendamentoCriacaoResponse> criar(@Valid @RequestBody AgendamentoCriacaoRequest request) {
        return ResponseEntity.status(201).body(service.criar(request));
    }

    @PatchMapping("/{idAgendamento}/cancelar")
    public ResponseEntity<Void> cancelarAgendamento(@PathVariable Long idAgendamento, @RequestBody CancelarAgendamentoRequest requestDto, @AuthenticationPrincipal UsuarioDetalhesDto usuario){
        service.cancelarAgendamento(idAgendamento, requestDto, usuario.getId());
        return ResponseEntity.status(200).build();
    }
}
