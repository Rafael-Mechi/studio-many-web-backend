package many.studio.web_backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoRequest;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.service.AgendamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<AgendamentoCriacaoResponse> criar(@Valid @RequestBody AgendamentoCriacaoRequest request, @Future LocalDateTime horario) {
        return ResponseEntity.status(201).body(service.criar(request, horario));
    }


}
