package many.studio.web_backend.controller;


import many.studio.web_backend.dto.pacote.PacoteListarDto;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.service.ServicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
