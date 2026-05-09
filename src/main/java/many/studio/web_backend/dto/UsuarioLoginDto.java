package many.studio.web_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioLoginDto {

    @NotBlank
    @Email
    @Schema(description = "E-mail do usuário", example = "noa@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "Senha do usuário", example = "123456")
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

