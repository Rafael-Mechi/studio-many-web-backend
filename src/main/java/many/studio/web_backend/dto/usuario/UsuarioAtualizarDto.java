package many.studio.web_backend.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public class UsuarioAtualizarDto {

    @Email
    @Schema(description = "Novo email do usuário", example = "novo@gmail.com")
    private String email;

    @Schema(description = "Nova senha do usuário", example = "novaSenha123")
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}