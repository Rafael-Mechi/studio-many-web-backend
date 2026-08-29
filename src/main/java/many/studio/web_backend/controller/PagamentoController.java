package many.studio.web_backend.controller;

import jakarta.validation.Valid;
import many.studio.web_backend.dto.pagamento.PagamentoRequest;
import many.studio.web_backend.dto.pagamento.PagamentoResponse;
import many.studio.web_backend.mapper.PagamentoMapper;
import many.studio.web_backend.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/sinal")
    public ResponseEntity<List<PagamentoResponse>> pagarSinal(@RequestBody List<Long> idAgendamentos, @Valid @RequestBody PagamentoRequest request) {
        return ResponseEntity.status(201).body(PagamentoMapper.toResponseList(pagamentoService.criarSinal(idAgendamentos, request)));
    }
}
