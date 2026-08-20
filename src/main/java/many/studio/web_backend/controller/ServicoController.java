package many.studio.web_backend.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import many.studio.web_backend.dto.pacote.PacoteCadastroDto;
import many.studio.web_backend.dto.pacote.PacoteListarDto;
import many.studio.web_backend.dto.servico.ServicoCadastroDto;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.mapper.PacoteMapper;
import many.studio.web_backend.mapper.ServicoMapper;
import many.studio.web_backend.service.ServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
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
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PacoteListarDto>> listarPacotesPorServico(@PathVariable Long id){
        return ResponseEntity.ok(PacoteMapper.toResponse(servicoService.listarPacotesPorServico(id)));
    }


    @PostMapping("/cadastrar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ServicoListarDto> cadastrar(@RequestBody ServicoCadastroDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ServicoMapper.toResponse(servicoService.criar(request)));
    }

    @GetMapping("/profissionais/{profissionalId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ServicoListarDto>> listarServicosPorProfissional(@PathVariable Long id){
        return ResponseEntity.ok(ServicoMapper.toResponse(servicoService.listarServicosPorProfissional(id)));
    }

    @PatchMapping("/servicos/{servicoId}/editar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ServicoListarDto> editar(@PathVariable Long id, ServicoCadastroDto dto ){
        return ResponseEntity.ok(ServicoMapper.toResponse(servicoService.editar(id,dto)));
    }

    @DeleteMapping("/servicos/{servicoId}/excluir")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        servicoService.deletar(id);
        return ResponseEntity.status(204).build();
    }
    @PatchMapping("/servicos/pacotes/{pacoteId}/editar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PacoteListarDto> editarPacote(@PathVariable Long pacoteId , PacoteCadastroDto dto ){
        return ResponseEntity.ok(PacoteMapper.toResponse(servicoService.editarPacote(pacoteId,dto)));
    }

    @DeleteMapping("/servicos/pacotes/{pacoteId}/excluir")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletarPacote(@PathVariable  Long pacoteId){
        servicoService.deletarPacote(pacoteId);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/servicos/{servicoId}/pacotes")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PacoteListarDto> cadastrarPacote(@PathVariable PacoteCadastroDto dto){
        return ResponseEntity.status(201).body(PacoteMapper.toResponse(servicoService.cadastrarPacote(dto)));
    }



}
