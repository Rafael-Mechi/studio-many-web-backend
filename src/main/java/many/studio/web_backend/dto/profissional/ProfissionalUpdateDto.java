package many.studio.web_backend.dto.profissional;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProfissionalUpdateDto(
        @Schema(description = "Nome atualizado do profissional", example = "Carlos Souza")
        String nome,

        @Email
        @Schema(description = "E-mail atualizado de acesso", example = "carlos.souza@email.com")
        String email,

        @Schema(description = "Telefone do profissional", example = "(11) 98765-4321")
        String telefone,

        @Schema(description = "Documento/CPF", example = "123.456.789-01")
        String documento,

        @Size(min = 6)
        @Schema(description = "Nova senha de acesso do usuário", example = "senha123")
        String senha
) {}