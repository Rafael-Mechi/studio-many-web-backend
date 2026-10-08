package many.studio.web_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import many.studio.web_backend.dto.config.BloqueioCriacaoResponse;
import many.studio.web_backend.dto.config.BloqueioRequest;
import many.studio.web_backend.dto.config.BloqueioResponse;
import many.studio.web_backend.dto.config.HorariosConfigRequest;
import many.studio.web_backend.dto.config.MeuPerfilUpdateRequest;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.dto.usuario.*;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.ForbiddenException;
import many.studio.web_backend.mapper.ServicoMapper;
import many.studio.web_backend.mapper.UsuarioMapper;
import many.studio.web_backend.service.AgendamentoService;
import many.studio.web_backend.service.BloqueioService;
import many.studio.web_backend.service.ContaService;
import many.studio.web_backend.service.HorarioTrabalhoService;
import many.studio.web_backend.service.ProfissionalService;
import many.studio.web_backend.service.ServicoService;
import many.studio.web_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    public static final String COOKIE_NOME = "authToken";

    @Value("${jwt.validity}")
    private long jwtValidity;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private HorarioTrabalhoService horarioTrabalhoService;

    @Autowired
    private BloqueioService bloqueioService;

    @Autowired
    private ContaService contaService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private ProfissionalService profissionalService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Void> criar(@RequestBody @Valid UsuarioCriacaoDto usuarioCriacaoDto) {

        this.usuarioService.criar(usuarioCriacaoDto);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(
            @RequestBody @Valid UsuarioTokenDto.UsuarioLoginDto usuarioLoginDto,
            HttpServletResponse response
    ) {

        final Usuario usuario = UsuarioMapper.of(usuarioLoginDto);

        UsuarioTokenDto autenticado = this.usuarioService.autenticar(usuario);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NOME, autenticado.getToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofSeconds(jwtValidity))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(autenticado);
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "Bearer")
    @Operation(
            summary = "Fazer logout")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "204",
                    description = "Logout é feito com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {}
                                    """)
                    )
            )
    })
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NOME, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Listar usuários")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Retorna a lista de usuários")
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Usuários são retornados com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                            [
                                              {
                                                "id": 1,
                                                "nome": "Giovana Rocha",
                                                "email": "giovana@outlook.com"
                                              },
                                              {
                                                "id": 2,
                                                "nome": "Márcia",
                                                "email": "marcia@gmail.com"
                                              }
                                            ]
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Nenhum usuário é encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {}
                                    """)
                    )
            )
    })
    @ApiResponse(
            responseCode = "401",
            description = "Não autorizado",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                  "timestamp": "2026-04-29T00:57:04.487+00:00",
                                  "status": 401,
                                  "error": "Unauthorized",
                                  "path": "/usuarios"
                                }
                            """)
            )
    )
    public ResponseEntity<List<UsuarioListarDto>> listarTodos() {

        List<UsuarioListarDto> usuariosEncontrados =
                this.usuarioService.listarTodos();

        if (usuariosEncontrados.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.ok(usuariosEncontrados);
    }

    @Operation(summary = "Atualizar usuário")
    @SecurityRequirement(name = "Bearer")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para cadastro",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "nome": "Marcela"
                            }
                            """)
            )
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "204",
                    description = "Usuários é atualizado com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                            {}
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhum usuário é encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2026-04-30T01:39:57.172+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/usuarios/22"
                                    }
                                    """)
                    )
            )
    })
    @ApiResponse(
            responseCode = "401",
            description = "Não autorizado",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                  "timestamp": "2026-04-29T00:57:04.487+00:00",
                                  "status": 401,
                                  "error": "Unauthorized",
                                  "path": "/usuarios/id"
                                }
                            """)
            )
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioAtualizarDto dto) {

        usuarioService.atualizar(id, dto);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deletar usuário")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuários é deletado com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                            {}
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhum usuário é encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2026-04-30T01:39:57.172+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/usuarios/22"
                                    }
                                    """)
                    )
            )
    })
    @ApiResponse(
            responseCode = "401",
            description = "Não autorizado",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                  "timestamp": "2026-04-29T00:57:04.487+00:00",
                                  "status": 401,
                                  "error": "Unauthorized",
                                  "path": "/usuarios/id"
                                }
                            """)
            )
    )
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerUsuario(@PathVariable Long id) {
        usuarioService.removerPorId(id);


        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Atualizar senha do usuário autenticado")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Senha atual e nova senha",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "senhaAtual": "123456",
                              "senhaNova": "novaSenha789"
                            }
                            """)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Senha atualizada com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                            {}
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado ou senha atual inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                        {
                                          "timestamp": "2026-04-29T00:57:04.487+00:00",
                                          "status": 401,
                                          "error": "Unauthorized",
                                          "path": "/usuarios/atualizar_senha"
                                        }
                                    """)
                    )
            )
    })
    @PutMapping("/atualizar_senha")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> atualizarSenha(@Valid @RequestBody UsuarioAtualizarSenhaDto dto) {
        usuarioService.atualizarSenha(dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Redefinir senha por e-mail")
    @SecurityRequirement(name = "Bearer")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "E-mail do usuário e nova senha",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "email": "giovana@outlook.com",
                              "senhaNova": "senhaRecuperada123"
                            }
                            """)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Senha redefinida com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                            {}
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2026-04-30T01:39:57.172+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/usuarios/redefinir_senha"
                                    }
                                    """)
                    )
            )
    })
    @PutMapping("/redefinir_senha")
    public ResponseEntity<Void> redefinirSenha(@Valid @RequestBody UsuarioRedefinirSenhaDto dto) {
        usuarioService.redefinirSenha(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Object> meuPerfil(Authentication authentication){
        UsuarioDetalhesDto detalhes = (UsuarioDetalhesDto) authentication.getPrincipal();

        Object dto = usuarioService.buscarMeuPerfil(detalhes);

        return ResponseEntity.status(200).body(dto);
    }

    @GetMapping("/me/agendamentos")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<VisaoGeralClienteResponse>> buscarAgendamentos(Authentication authentication) {
        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();

        Long id = usuario.getId();
        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        return ResponseEntity.ok(agendamentoService.buscarAgendamentos(id, role));
    }

    @GetMapping("/clientes")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ClienteResponse>> getClientes(Authentication authentication) {
        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();

        Long id = usuario.getId();
        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        return ResponseEntity.ok(usuarioService.getClientes(id, role));
    }

    @GetMapping("/profissionais")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ProfissionalResponse>> getProfissionais(Authentication authentication) {
        UsuarioDetalhesDto usuario = (UsuarioDetalhesDto) authentication.getPrincipal();

        Long id = usuario.getId();
        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        return ResponseEntity.ok(usuarioService.getProfissionais(id, role));
    }

    @Operation(summary = "Atualizar nome e telefone do usuário autenticado")
    @PatchMapping("/me")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Object> atualizarMeuPerfil(
            @AuthenticationPrincipal UsuarioDetalhesDto usuario,
            @Valid @RequestBody MeuPerfilUpdateRequest dto) {
        usuarioService.atualizarMeuPerfil(usuario.getId(), dto);
        return ResponseEntity.ok(usuarioService.buscarMeuPerfil(usuario));
    }

    @Operation(summary = "Ver jornada semanal do profissional autenticado")
    @GetMapping("/me/horarios")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<HorariosConfigRequest> meusHorarios(
            @AuthenticationPrincipal UsuarioDetalhesDto usuario) {
        exigirProfissional(usuario);
        return ResponseEntity.ok(horarioTrabalhoService.listar(usuario.getId()));
    }

    @Operation(summary = "Atualizar jornada semanal do profissional autenticado")
    @PatchMapping("/me/horarios")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<HorariosConfigRequest> atualizarMeusHorarios(
            @AuthenticationPrincipal UsuarioDetalhesDto usuario,
            @Valid @RequestBody HorariosConfigRequest dto) {
        exigirProfissional(usuario);
        return ResponseEntity.ok(horarioTrabalhoService.salvar(usuario.getId(), dto));
    }

    @Operation(summary = "Listar bloqueios de horário do profissional autenticado")
    @GetMapping("/me/bloqueios")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<BloqueioResponse>> meusBloqueios(
            @AuthenticationPrincipal UsuarioDetalhesDto usuario) {
        exigirProfissional(usuario);
        return ResponseEntity.ok(bloqueioService.listar(usuario.getId()));
    }

    @Operation(summary = "Criar bloqueio de horário do profissional autenticado")
    @PostMapping("/me/bloqueios")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<BloqueioCriacaoResponse> criarBloqueio(
            @AuthenticationPrincipal UsuarioDetalhesDto usuario,
            @Valid @RequestBody BloqueioRequest dto) {
        exigirProfissional(usuario);
        return ResponseEntity.status(201).body(bloqueioService.criar(usuario.getId(), dto));
    }

    @Operation(summary = "Excluir bloqueio de horário do profissional autenticado")
    @DeleteMapping("/me/bloqueios/{bloqueioId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> excluirBloqueio(
            @AuthenticationPrincipal UsuarioDetalhesDto usuario,
            @PathVariable Long bloqueioId) {
        exigirProfissional(usuario);
        bloqueioService.excluir(usuario.getId(), bloqueioId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar serviços vinculados ao profissional autenticado")
    @GetMapping("/me/servicos")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ServicoListarDto>> meusServicos(
            @AuthenticationPrincipal UsuarioDetalhesDto usuario) {
        exigirProfissional(usuario);
        Long profissionalId = profissionalService.findByUsuarioId(usuario.getId()).getId();
        return ResponseEntity.ok(ServicoMapper.toResponse(
                servicoService.listarServicosPorProfissional(profissionalId)));
    }

    @Operation(summary = "Apagar conta (soft delete) e cancelar agendamentos futuros")
    @PatchMapping("/me/desativar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> desativarMinhaConta(
            @AuthenticationPrincipal UsuarioDetalhesDto usuario,
            HttpServletResponse response) {
        contaService.desativarMinhaConta(usuario.getId());

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NOME, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.noContent().build();
    }

    private void exigirProfissional(UsuarioDetalhesDto usuario) {
        String role = usuario.getAuthorities()
                .iterator()
                .next()
                .getAuthority();
        if (!"ROLE_PROFISSIONAL".equalsIgnoreCase(role) && !"ROLE_ADMIN".equalsIgnoreCase(role)) {
            throw new ForbiddenException("Você não tem permissão para acessar esse recurso");
        }
    }

//    @Operation(summary = "Atualizar perfil do usuário autenticado")
//    @io.swagger.v3.oas.annotations.parameters.RequestBody(
//            description = "Dados do perfil (campos opcionais)",
//            required = true,
//            content = @Content(
//                    mediaType = "application/json",
//                    examples = @ExampleObject(value = """
//                            {
//                              "nome": "Giovana Rocha",
//                              "telefone": "11999998888",
//                              "documento": "12345678900",
//                              "email": "giovana.nova@outlook.com"
//                            }
//                            """)
//            )
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "204",
//                    description = "Perfil atualizado com êxito",
//                    content = @Content(
//                            mediaType = "application/json",
//                            examples = @ExampleObject(value = """
//                                            {}
//                                    """)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Nenhum campo para atualizar",
//                    content = @Content(
//                            mediaType = "application/json",
//                            examples = @ExampleObject(value = """
//                                    {
//                                      "timestamp": "2026-04-30T01:39:57.172+00:00",
//                                      "status": 400,
//                                      "error": "Bad Request",
//                                      "path": "/usuarios/atualizar_perfil"
//                                    }
//                                    """)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Usuário ou dados complementares não encontrados",
//                    content = @Content(
//                            mediaType = "application/json",
//                            examples = @ExampleObject(value = """
//                                    {
//                                      "timestamp": "2026-04-30T01:39:57.172+00:00",
//                                      "status": 404,
//                                      "error": "Not Found",
//                                      "path": "/usuarios/atualizar_perfil"
//                                    }
//                                    """)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "409",
//                    description = "E-mail já cadastrado",
//                    content = @Content(
//                            mediaType = "application/json",
//                            examples = @ExampleObject(value = """
//                                    {
//                                      "timestamp": "2026-04-30T01:39:57.172+00:00",
//                                      "status": 409,
//                                      "error": "Conflict",
//                                      "path": "/usuarios/atualizar_perfil"
//                                    }
//                                    """)
//                    )
//            )
//    })
//    @ApiResponse(
//            responseCode = "401",
//            description = "Não autorizado",
//            content = @Content(
//                    mediaType = "application/json",
//                    examples = @ExampleObject(value = """
//                                {
//                                  "timestamp": "2026-04-29T00:57:04.487+00:00",
//                                  "status": 401,
//                                  "error": "Unauthorized",
//                                  "path": "/usuarios/atualizar_perfil"
//                                }
//                            """)
//            )
//    )
//    @PutMapping("/atualizar_perfil")
//    @SecurityRequirement(name = "Bearer")
//    public ResponseEntity<Void> atualizarPerfil(@Valid @RequestBody UsuarioAtualizarPerfilDto dto) {
//        usuarioService.atualizarPerfil(dto);
//        return ResponseEntity.noContent().build();
//    }
}
