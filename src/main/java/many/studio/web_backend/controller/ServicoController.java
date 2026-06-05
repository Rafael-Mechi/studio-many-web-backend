package many.studio.web_backend.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import many.studio.web_backend.dto.pacote.PacoteListarDto;
import many.studio.web_backend.dto.servico.ServicoCadastroDto;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.service.ServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servico")
public class ServicoController {


    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }



    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ServicoListarDto>> listar(){
        return ResponseEntity.ok(servicoService.listar());
    }

    @GetMapping("/{servicoId}/pacotes")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PacoteListarDto>> listarPacotesPorServico(@PathVariable Long id){
        return ResponseEntity.ok(servicoService.listarPacotesPorServico(id));
    }

    @PostMapping("/cadastrar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ServicoListarDto> cadastrar(@RequestBody ServicoCadastroDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.criar(request));
    }



}
