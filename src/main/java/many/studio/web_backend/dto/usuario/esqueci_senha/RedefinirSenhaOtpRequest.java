package many.studio.web_backend.dto.usuario.esqueci_senha;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RedefinirSenhaOtpRequest {

    @NotBlank
    @Size(min = 6, max = 20)
    private String novaSenha;

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}
