package many.studio.web_backend.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import many.studio.web_backend.dto.config.HorariosConfigRequest;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

public class ProfissionalCadastroDto {

    @Size(min = 3, max = 75)
    @Schema(description = "Nome do profissional", example = "Ana Lima")
    private String nome;

    @Email
    @Schema(description = "Email do profissional", example = "ana@lumiere.com")
    private String email;

    @Size(min = 6, max = 20)
    @Schema(description = "Senha inicial do profissional", example = "123456")
    private String senha;

    @Schema(description = "Telefone do profissional", example = "11987654321")
    private String telefone;

    @CPF
    @Schema(description = "CPF do profissional", example = "52155468865")
    private String documento;

    @Size(max = 255)
    @Schema(description = "URL da foto do profissional (POST /usuarios/imagem antes)", example = "http://localhost:8080/uploads/usuarios/uuid.jpg")
    private String fotoUrl;

    @Valid
    @Schema(description = "Jornada inicial opcional (mesmo formato de PATCH /usuarios/me/horarios). Se omitido, profissional começa sem disponibilidade.")
    private HorariosConfigRequest horarios;

    @Schema(description = "Ids dos servicos que o profissional realiza (GET /servicos). Opcional.", example = "[1, 2]")
    private List<Long> servicosIds;

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

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public HorariosConfigRequest getHorarios() {
        return horarios;
    }

    public void setHorarios(HorariosConfigRequest horarios) {
        this.horarios = horarios;
    }

    public List<Long> getServicosIds() {
        return servicosIds;
    }

    public void setServicosIds(List<Long> servicosIds) {
        this.servicosIds = servicosIds;
    }
}
