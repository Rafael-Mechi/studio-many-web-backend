package many.studio.web_backend.controller;

import many.studio.web_backend.dto.agendamento.AgendamentoAnaliseResponse;
import many.studio.web_backend.service.AnaliseAgendamentosService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
