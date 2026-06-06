package many.studio.web_backend.controller;


import many.studio.web_backend.dto.pacote.PacoteCadastroDto;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.cadastrar(request));
    }

    @GetMapping("/profissionais/{profissionalId}")
    public ResponseEntity<List<ServicoListarDto>> listarServicosPorProfissional(@PathVariable Long id){
        return ResponseEntity.ok(servicoService.listarServicosPorProfissional(id));
    }

    @PatchMapping("/servicos/{servicoId}/editar")
    public ResponseEntity<ServicoListarDto> editar(@PathVariable Long id, ServicoCadastroDto dto ){
        return ResponseEntity.ok(servicoService.editar(id,dto));
    }

    @DeleteMapping("/servicos/{servicoId}/excluir")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        servicoService.deletar(id);
        return ResponseEntity.status(204).build();
    }
    @PatchMapping("/servicos/pacotes/{pacoteId}/editar")
    public ResponseEntity<PacoteListarDto> editarPacote(@PathVariable Long pacoteId , PacoteCadastroDto dto ){
        return ResponseEntity.ok(servicoService.editarPacote(pacoteId,dto));
    }

    @DeleteMapping("/servicos/pacotes/{pacoteId}/excluir")
    public ResponseEntity<Void> deletarPacote(@PathVariable  Long pacoteId){
        servicoService.deletarPacote(pacoteId);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/servicos/{servicoId}/pacotes")
    public ResponseEntity<PacoteListarDto> cadastrarPacote(@PathVariable PacoteCadastroDto dto){
        return ResponseEntity.status(201).body(servicoService.cadastrarPacote(dto));
    }



}
