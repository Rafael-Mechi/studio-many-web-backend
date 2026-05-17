package many.studio.web_backend.controller;

import jakarta.validation.Valid;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoRequest;
import many.studio.web_backend.dto.agendamento.AgendamentoCriacaoResponse;
import many.studio.web_backend.mapper.AgendamentoMapper;
import many.studio.web_backend.service.AgendamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<AgendamentoCriacaoResponse> criar(@Valid @RequestBody AgendamentoCriacaoRequest request) {
        return ResponseEntity.status(201).body(service.criar(AgendamentoMapper.toEntity(request), request.getHorarios()));
    }


}
