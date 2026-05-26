package many.studio.web_backend.controller;


import many.studio.web_backend.dto.pacote.PacoteListarDto;
import many.studio.web_backend.dto.servico.ServicoCadastroDto;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.service.ServicoService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<ServicoListarDto>> listar(){
        return ResponseEntity.ok(servicoService.listar());
    }

    @GetMapping("/{servicoId}/pacotes")
    public ResponseEntity<List<PacoteListarDto>> listarPacotesPorServico(@PathVariable Long id){
        return ResponseEntity.ok(servicoService.listarPacotesPorServico(id));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ServicoListarDto> cadastrar(@RequestBody ServicoCadastroDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.criar(request));
    }

    @GetMapping("/{servicoId}/horarios_disponiveis")
    public ResponseEntity<List<ServicoListarDto>> listarPacotesPorHorarioDisponivel(@PathVariable Long id){
        return ResponseEntity.ok(servicoService.listarServicoPorHorarioDisponivel(id));
    }

    @GetMapping("/profissionais/{profissionalId}")
    public ResponseEntity<List<ServicoListarDto>> listarServicosPorProfissional(@PathVariable Long id){
        return ResponseEntity.ok(servicoService.listarServicosPorProfissional(id));
    }





}
