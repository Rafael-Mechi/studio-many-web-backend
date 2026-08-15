package many.studio.web_backend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import many.studio.web_backend.dto.agendamento.*;
import many.studio.web_backend.dto.usuario.UsuarioDetalhesDto;
import many.studio.web_backend.mapper.agendamento.AgendamentoItemMapper;
import many.studio.web_backend.mapper.agendamento.AgendamentoMapper;
import many.studio.web_backend.service.AgendamentoItemService;
import many.studio.web_backend.service.AgendamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final AgendamentoItemService agendamentoItemService;

    public AgendamentoController(AgendamentoService agendamentoService, AgendamentoItemService agendamentoItemService) {
        this.agendamentoService = agendamentoService;
        this.agendamentoItemService = agendamentoItemService;
    }


    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<AgendamentoResponse>> buscarTodos() {
        return ResponseEntity.ok(AgendamentoMapper.toAgendamentoResponseList(agendamentoService.buscarTodos()));
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoMapper.toAgendamentoResponse(agendamentoService.buscarPorId(id)));
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<AgendamentoCriacaoResponse> criar(@Valid @RequestBody AgendamentoCriacaoRequest request) {
        return ResponseEntity.status(201).body(agendamentoService.criar(request, request.getHorario()));
    }

    @PatchMapping("/{idAgendamento}/cancelar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> cancelarAgendamento(@PathVariable Long idAgendamento, @RequestBody CancelarAgendamentoRequest requestDto, @AuthenticationPrincipal UsuarioDetalhesDto usuario){
        agendamentoService.cancelarAgendamento(idAgendamento, requestDto, usuario.getId());
        return ResponseEntity.status(200).build();
    }

    @PatchMapping("/{itemId}/reagendar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<AgendamentoItemResponse> reagendar(@PathVariable Long itemId, LocalDateTime novoHorario) {
        return ResponseEntity.ok(AgendamentoItemMapper.toResponse(agendamentoItemService.reagendar(itemId, novoHorario)));
    }

    @PatchMapping("/{idAgendamento}/confirmar")
    public ResponseEntity<Void> confirmar(@PathVariable Long idAgendamento, @AuthenticationPrincipal UsuarioDetalhesDto usuario) {

        agendamentoService.confirmar(idAgendamento, usuario);

        return ResponseEntity.ok().build();
    }
}
