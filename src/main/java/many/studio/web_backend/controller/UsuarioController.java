package many.studio.web_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import many.studio.web_backend.dto.usuario.*;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.mapper.UsuarioMapper;
import many.studio.web_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
            @PathVariable Integer id,
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
    public ResponseEntity<Void> removerUsuario(@PathVariable Integer id) {
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

    @Operation(summary = "Atualizar perfil do usuário autenticado")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do perfil (campos opcionais)",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "nome": "Giovana Rocha",
                              "telefone": "11999998888",
                              "documento": "12345678900",
                              "email": "giovana.nova@outlook.com"
                            }
                            """)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Perfil atualizado com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                            {}
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nenhum campo para atualizar",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2026-04-30T01:39:57.172+00:00",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "path": "/usuarios/atualizar_perfil"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário ou dados complementares não encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2026-04-30T01:39:57.172+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/usuarios/atualizar_perfil"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "E-mail já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2026-04-30T01:39:57.172+00:00",
                                      "status": 409,
                                      "error": "Conflict",
                                      "path": "/usuarios/atualizar_perfil"
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
                                  "path": "/usuarios/atualizar_perfil"
                                }
                            """)
            )
    )
    @PutMapping("/atualizar_perfil")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> atualizarPerfil(@Valid @RequestBody UsuarioAtualizarPerfilDto dto) {
        usuarioService.atualizarPerfil(dto);
        return ResponseEntity.noContent().build();
    }
}
