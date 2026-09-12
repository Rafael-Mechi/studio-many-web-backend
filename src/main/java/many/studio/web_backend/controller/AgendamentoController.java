package many.studio.web_backend.controller;

import com.twilio.http.Response;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import many.studio.web_backend.dto.agendamento.*;
import many.studio.web_backend.dto.comprovante.ComprovanteResponse;
import many.studio.web_backend.dto.selecao_agendamento.DisponibilidadeRequest;
import many.studio.web_backend.dto.selecao_agendamento.DisponibilidadeResponse;
import many.studio.web_backend.dto.usuario.UsuarioDetalhesDto;
import many.studio.web_backend.mapper.agendamento.AgendamentoItemMapper;
import many.studio.web_backend.mapper.agendamento.AgendamentoMapper;
import many.studio.web_backend.mapper.comprovante.ComprovanteMapper;
import many.studio.web_backend.service.AgendamentoItemService;
import many.studio.web_backend.service.AgendamentoService;
import many.studio.web_backend.service.DisponibilidadeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final AgendamentoItemService agendamentoItemService;
    private final DisponibilidadeService disponibilidadeService;

    public AgendamentoController(AgendamentoService agendamentoService, AgendamentoItemService agendamentoItemService, DisponibilidadeService disponibilidadeService) {
        this.agendamentoService = agendamentoService;
        this.agendamentoItemService = agendamentoItemService;
        this.disponibilidadeService = disponibilidadeService;
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

    //ESSE ENDPOINT ESTÁ SALVANDO O PDF DO COMPROVANTE NO BANCO NA TABELA "comprovante_prv". DESFAZER ESSA MUDANÇA QUANDO HOUVER PDF NO BUCKET
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer")
    @RequestBody(content = @Content(
            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
            encoding = @Encoding(
                    name = "request",
                    contentType = MediaType.APPLICATION_JSON_VALUE
            )
    ))
    public ResponseEntity<List<AgendamentoCriacaoResponse>> criar(@Valid @RequestPart("request") AgendamentosCriacaoRequest request,
                                                                  @RequestPart("pdf") MultipartFile pdf,
                                                                  Authentication authentication) throws IOException {
        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();

        Long id = usuario.getId();
        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();
        return ResponseEntity.status(201).body(agendamentoService.criar(id, role, request.getAgendamentos(), pdf));
    }

    // ENDPOINT PARA EXIBIR COMPROVANTE COM A ABORDAGEM PROVISÓRIA ENQUANTO NAO TIVER PDF NO BUCKET
    @GetMapping("/{idAgendamento}/comprovante")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ComprovanteResponse>getComprovante(@PathVariable Long idAgendamento){
        return ResponseEntity.status(200).body(ComprovanteMapper.toResponse(agendamentoService.getComprovante(idAgendamento)));
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

    @PostMapping("/disponibilidade")
    public ResponseEntity<DisponibilidadeResponse> disponibilidade(@RequestBody DisponibilidadeRequest disponibilidadeRequest){
        DisponibilidadeResponse response = disponibilidadeService.calcular(disponibilidadeRequest);

        return ResponseEntity.status(200).body(response);
    }
}
