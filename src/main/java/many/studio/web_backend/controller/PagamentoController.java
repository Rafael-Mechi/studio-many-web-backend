package many.studio.web_backend.controller;

import jakarta.validation.Valid;
import many.studio.web_backend.dto.pagamento.PagamentoRequest;
import many.studio.web_backend.dto.pagamento.PagamentoResponse;
import many.studio.web_backend.mapper.PagamentoMapper;
import many.studio.web_backend.service.PagamentoService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PagamentoResponse>> pagarSinal(@RequestParam("comprovante") MultipartFile comprovante,
                                                              @RequestParam List<Long> idAgendamentos) {

        if(comprovante == null || comprovante.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.status(201).body(PagamentoMapper.toResponseList(pagamentoService.criarSinal(idAgendamentos)));
    }
}
