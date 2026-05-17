package many.studio.web_backend.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UsuarioAtualizarPerfilDto {
    @Email
    private String email;

    @Size(min = 3, max = 75)
    private String nome;

    private String telefone;
    private String documento;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
}
