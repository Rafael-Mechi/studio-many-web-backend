package many.studio.web_backend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import many.studio.web_backend.dto.ComprovanteResponse;
import many.studio.web_backend.dto.agendamento.*;
import many.studio.web_backend.dto.selecao_agendamento.DisponibilidadeRequest;
import many.studio.web_backend.dto.selecao_agendamento.DisponibilidadeResponse;
import many.studio.web_backend.dto.usuario.UsuarioDetalhesDto;
import many.studio.web_backend.entity.Comprovante;
import many.studio.web_backend.mapper.agendamento.AgendamentoItemMapper;
import many.studio.web_backend.mapper.agendamento.AgendamentoMapper;
import many.studio.web_backend.service.AgendamentoItemService;
import many.studio.web_backend.service.AgendamentoService;
import many.studio.web_backend.service.DisponibilidadeService;
import many.studio.web_backend.service.PagamentoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final AgendamentoItemService agendamentoItemService;
    private final DisponibilidadeService disponibilidadeService;
    private final PagamentoService pagamentoService;

    public AgendamentoController(AgendamentoService agendamentoService, AgendamentoItemService agendamentoItemService, DisponibilidadeService disponibilidadeService, PagamentoService pagamentoService) {
        this.agendamentoService = agendamentoService;
        this.agendamentoItemService = agendamentoItemService;
        this.disponibilidadeService = disponibilidadeService;
        this.pagamentoService = pagamentoService;
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
    public ResponseEntity<List<AgendamentoCriacaoResponse>> criar(@Valid @RequestBody List<AgendamentoCriacaoRequest> request, Authentication authentication) {
        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();

        Long id = usuario.getId();
        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();
        return ResponseEntity.status(201).body(agendamentoService.criar(id, role, request));
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

    @PatchMapping("/{idAgendamento}/checkIn")
    public ResponseEntity<Void> fazerCheckIn(@PathVariable Long idAgendamento, @AuthenticationPrincipal UsuarioDetalhesDto usuario) {

        agendamentoService.confirmar(idAgendamento, usuario);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{idAgendamento}/emAtendimento")
    public ResponseEntity<Void> emAtendimento(@PathVariable Long idAgendamento, @AuthenticationPrincipal UsuarioDetalhesDto usuario) {

        agendamentoService.confirmar(idAgendamento, usuario);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{idAgendamento}/concluido")
    public ResponseEntity<Void> concluir(@PathVariable Long idAgendamento, @AuthenticationPrincipal UsuarioDetalhesDto usuario) {

        agendamentoService.confirmar(idAgendamento, usuario);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/disponibilidade")
    public ResponseEntity<DisponibilidadeResponse> disponibilidade(@RequestBody DisponibilidadeRequest disponibilidadeRequest){
        DisponibilidadeResponse response = disponibilidadeService.calcular(disponibilidadeRequest);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}/comprovante")
    public ResponseEntity<ComprovanteResponse> buscarComprovante(
            @PathVariable Long id) {

        Comprovante comprovante =
                pagamentoService.buscarComprovantePorAgendamento(id);

        ComprovanteResponse response = new ComprovanteResponse(
                comprovante.getArquivo(),
                comprovante.getTipoArquivo()
        );

        return ResponseEntity.ok(response);
    }
}
