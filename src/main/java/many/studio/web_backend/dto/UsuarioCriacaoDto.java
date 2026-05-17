package many.studio.web_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioCriacaoDto {

    @Size(min = 3, max = 10)
    @Schema(description = "Nome do usuário", example = "noa")
    private String nome;

    @Email
    @Schema(description = "Email do usuário", example = "noa@gmail.com")
    private String email;

    @Size(min = 6, max = 20)
    @Schema(description = "Senha do usuário", example = "123456")
    private String senha;

    @Schema(description = "Telefone do usuário", example = "987654321")
    private String telefone;

    @Schema(description = "cpf do usuário", example = "54345743331")
    private String documento;

    @NotNull
    @Schema(description = "Perfil do usuário", example = "1")
    private Long perfilId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Long getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(Long perfilId) {
        this.perfilId = perfilId;
    }
}