package many.studio.web_backend.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

public class ProfissionalCadastroResponse {

    @Schema(description = "Id do usuário criado", example = "10")
    private Long usuarioId;

    @Schema(description = "Id do profissional criado", example = "4")
    private Long profissionalId;

    public ProfissionalCadastroResponse() {
    }

    public ProfissionalCadastroResponse(Long usuarioId, Long profissionalId) {
        this.usuarioId = usuarioId;
        this.profissionalId = profissionalId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }
}
