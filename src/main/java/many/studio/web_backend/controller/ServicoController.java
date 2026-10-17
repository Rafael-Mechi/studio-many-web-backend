package many.studio.web_backend.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import many.studio.web_backend.dto.pacote.PacoteCadastroDto;
import many.studio.web_backend.dto.pacote.PacoteListarDto;
import many.studio.web_backend.dto.servico.CategoriaServicoCadastroDto;
import many.studio.web_backend.dto.servico.CategoriaServicoDto;
import many.studio.web_backend.dto.servico.ServicoCadastroDto;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.dto.servico.ServicoStatusDto;
import many.studio.web_backend.mapper.PacoteMapper;
import many.studio.web_backend.mapper.ServicoMapper;
import many.studio.web_backend.service.ArmazenamentoImagemService;
import many.studio.web_backend.service.ServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;
    private final ArmazenamentoImagemService armazenamentoImagemService;

    public ServicoController(ServicoService servicoService, ArmazenamentoImagemService armazenamentoImagemService) {
        this.servicoService = servicoService;
        this.armazenamentoImagemService = armazenamentoImagemService;
    }


    @GetMapping
    public ResponseEntity<List<ServicoListarDto>> listar(){
        return ResponseEntity.ok(servicoService.listar());
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicoListarDto> cadastrar(@Valid @RequestBody ServicoCadastroDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.criar(request));
    }

    @GetMapping("/profissionais/{profissionalId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ServicoListarDto>> listarServicosPorProfissional(@PathVariable Long profissionalId){
        return ResponseEntity.ok(ServicoMapper.toResponse(servicoService.listarServicosPorProfissional(profissionalId)));
    }

    @PatchMapping("/{servicoId}/editar")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicoListarDto> editar(@PathVariable Long servicoId, @Valid @RequestBody ServicoCadastroDto dto){
        return ResponseEntity.ok(servicoService.editar(servicoId, dto));
    }

    @PatchMapping("/{servicoId}/status")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicoListarDto> alterarStatus(@PathVariable Long servicoId, @Valid @RequestBody ServicoStatusDto dto){
        return ResponseEntity.ok(servicoService.alterarStatus(servicoId, dto.getAtivo()));
    }

    @DeleteMapping("/{servicoId}/excluir")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long servicoId){
        servicoService.deletar(servicoId);
        return ResponseEntity.status(204).build();
    }

    @PostMapping(value = "/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> enviarImagem(@RequestParam("arquivo") MultipartFile arquivo){
        String nomeArquivo = armazenamentoImagemService.salvarImagemServico(arquivo);

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/servicos/")
                .path(nomeArquivo)
                .toUriString();

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("url", url));
    }


    @GetMapping("/categorias")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoriaServicoDto>> listarCategorias(){
        return ResponseEntity.ok(servicoService.listarCategorias());
    }

    @PostMapping("/categorias")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaServicoDto> cadastrarCategoria(@Valid @RequestBody CategoriaServicoCadastroDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.criarCategoria(dto));
    }


    @GetMapping("/{servicoId}/pacotes")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PacoteListarDto>> listarPacotesPorServico(@PathVariable Long servicoId){
        return ResponseEntity.ok(PacoteMapper.toResponse(servicoService.listarPacotesPorServico(servicoId)));
    }

    @GetMapping("/{servicoId}/pacotes/gerenciar")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PacoteListarDto>> listarTodosPacotesPorServico(@PathVariable Long servicoId){
        return ResponseEntity.ok(PacoteMapper.toResponse(servicoService.listarTodosPacotesPorServico(servicoId)));
    }

    @PostMapping("/{servicoId}/pacotes")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PacoteListarDto> cadastrarPacote(@PathVariable Long servicoId, @Valid @RequestBody PacoteCadastroDto dto){
        return ResponseEntity.status(201).body(PacoteMapper.toResponse(servicoService.cadastrarPacote(servicoId, dto)));
    }

    @PatchMapping("/pacotes/{pacoteId}/editar")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PacoteListarDto> editarPacote(@PathVariable Long pacoteId, @Valid @RequestBody PacoteCadastroDto dto){
        return ResponseEntity.ok(PacoteMapper.toResponse(servicoService.editarPacote(pacoteId, dto)));
    }

    @DeleteMapping("/pacotes/{pacoteId}/excluir")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarPacote(@PathVariable Long pacoteId){
        servicoService.deletarPacote(pacoteId);
        return ResponseEntity.status(204).build();
    }

}
