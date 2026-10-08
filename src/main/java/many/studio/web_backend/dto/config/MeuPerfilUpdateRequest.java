package many.studio.web_backend.dto.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MeuPerfilUpdateRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 75, message = "Nome deve ter entre 3 e 75 caracteres")
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(min = 10, max = 20, message = "Telefone inválido")
    private String telefone;

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
}
